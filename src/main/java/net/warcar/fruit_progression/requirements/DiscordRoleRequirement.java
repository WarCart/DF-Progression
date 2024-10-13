package net.warcar.fruit_progression.requirements;

import com.google.gson.JsonObject;
import de.erdbeerbaerlp.dcintegration.common.DiscordIntegration;
import de.erdbeerbaerlp.dcintegration.common.storage.linking.LinkManager;
import de.erdbeerbaerlp.dcintegration.common.storage.linking.PlayerLink;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import net.warcar.fruit_progression.data.entity.abilities_addition.AbilityAdditionDataCapability;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;

import java.util.Optional;
import java.util.UUID;

public class DiscordRoleRequirement extends Requirement {
    public DiscordRoleRequirement() {
        super(Long.TYPE);
    }

    public boolean requirementMet(LivingEntity entity, AbilityCore<?> core, RequirementInstance instance) {
        Optional<PlayerLink> link = getOrWarnPlayerLink(entity.getUUID());
        if (link.isPresent()) {
            Member member = DiscordIntegration.INSTANCE.getMemberById(link.get().discordID);
            if (member == null) {
                DevilFruitProgressionMod.LOGGER.warn("Member with Discord ID {} could not be found. Unlinking.", link.get().discordID);
                LinkManager.unlinkPlayer(link.get().discordID);
                return false;
            }
            for (Role role : member.getRoles()) {
                if (role.getIdLong() == Long.decode(instance.getValues()[0])) {
                    return true;
                }
            }
        }
        return false;
    }

    public RequirementInstance deserializeInstance(JsonObject json) {
        RequirementInstance instance = new RequirementInstance(this);
        instance.setValues(String.valueOf(json.get("discordRole").getAsLong()));
        return instance;
    }

    public static Optional<PlayerLink> getOrWarnPlayerLink(UUID uuid) {
        if (LinkManager.isPlayerLinked(uuid)) {
            return Optional.of(LinkManager.getLink(null, uuid));
        } else {
            DevilFruitProgressionMod.LOGGER.warn(
                    "User with the UUID {} is not Linked! Consider enforcing linking in Discord Integration's Config.",
                    uuid);
        }
        return Optional.empty();
    }

}
