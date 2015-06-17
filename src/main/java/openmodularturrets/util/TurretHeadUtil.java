package openmodularturrets.util;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import openmodularturrets.compatability.ModCompatibility;
import openmodularturrets.handler.ConfigHandler;
import openmodularturrets.items.addons.*;
import openmodularturrets.items.upgrades.*;
import openmodularturrets.tileentity.turretbase.TrustedPlayer;
import openmodularturrets.tileentity.turretbase.TurretBase;
import openmodularturrets.tileentity.turretbase.TurretBaseTierOneTileEntity;
import openmodularturrets.tileentity.turrets.TurretHead;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class TurretHeadUtil {

    public static HashSet<EntityPlayerMP> warnedPlayers = new HashSet<EntityPlayerMP>();

    public static void warnPlayers(TurretBase base, World worldObj,
                                   int downLowAmount, int xCoord, int yCoord, int zCoord,
                                   int turretRange) {
        if (base.isAttacksPlayers()) {
            int warnDistance = ConfigHandler.getTurretWarningDistance();
            AxisAlignedBB axis = AxisAlignedBB.getBoundingBox(xCoord
                    - turretRange - warnDistance, yCoord - downLowAmount
                    - warnDistance, zCoord - turretRange - warnDistance, xCoord
                    + turretRange + warnDistance, yCoord + turretRange
                    + warnDistance, zCoord + turretRange + warnDistance);

            if (worldObj.getWorldTime() % 200 == 0) {
                warnedPlayers.clear();
            }

            List<EntityPlayerMP> targets = worldObj.getEntitiesWithinAABB(
                    EntityPlayerMP.class, axis);

            for (EntityPlayerMP target : targets) {
                if (!target.getUniqueID().toString().equals(base.getOwner())
                        && !isTrustedPlayer(target.getUniqueID(), base) && !warnedPlayers.contains(target) && !target.capabilities.isCreativeMode) {
                    dispatchWarnMessage(target, worldObj);
                    warnedPlayers.add(target);
                }
            }
        }
    }

    public static void dispatchWarnMessage(EntityPlayerMP player, World worldObj) {
        if (ConfigHandler.turretAlarmSound) {
            worldObj.playSoundEffect(player.posX, player.posY, player.posZ,
                    "openmodularturrets:warning", 1.0F, 1.0F);
        }
        if (ConfigHandler.turretWarnMessage) {
            player.addChatMessage(new ChatComponentText(
                    EnumChatFormatting.DARK_RED
                            + StatCollector.translateToLocal("status.warning")));
        }
    }

    public static Entity getTarget(TurretBase base, World worldObj,
                                   int downLowAmount, int xCoord, int yCoord, int zCoord,
                                   int turretRange, TurretHead turret) {

        Entity target = null;

        if (!worldObj.isRemote && base != null && base.getOwner() != null) {
            AxisAlignedBB axis = AxisAlignedBB.getBoundingBox(xCoord
                            - turretRange, yCoord - downLowAmount,
                    zCoord - turretRange, xCoord + turretRange, yCoord
                            + turretRange, zCoord + turretRange);

            List<EntityLivingBase> targets = worldObj.getEntitiesWithinAABB(
                    EntityLivingBase.class, axis);

            for (EntityLivingBase target1 : targets) {

                if (base.isAttacksNeutrals() && ConfigHandler.globalCanTargetNeutrals) {
                    if (target1 instanceof EntityAnimal && !target1.isDead) {
                        target = target1;
                    }
                }

                if (base.isAttacksNeutrals() && ConfigHandler.globalCanTargetNeutrals) {
                    if (target1 instanceof EntityAmbientCreature
                            && !target1.isDead) {
                        target = target1;
                    }
                }

                if (base.isAttacksMobs() && ConfigHandler.globalCanTargetMobs) {
                    if (target1 instanceof IMob && !target1.isDead) {
                        target = target1;
                    }
                }

                if (base.isAttacksPlayers() && ConfigHandler.globalCanTargetPlayers) {
                    if (target1 instanceof EntityPlayerMP && !target1.isDead) {
                        EntityPlayerMP entity = (EntityPlayerMP) target1;

                        if (!entity.getUniqueID().toString().equals(base.getOwner())
                                && !isTrustedPlayer(entity.getUniqueID(),
                                base) && !entity.capabilities.isCreativeMode) {
                            target = target1;
                        }
                    }
                }

                if (target != null && turret != null) {

                    EntityLivingBase targetELB = (EntityLivingBase) target;

                    if (canTurretSeeTarget(turret, targetELB)
                            && targetELB.getHealth() > 0.0F) {
                        return target;
                    }
                }
            }
        }
        return null;
    }

    public static Entity getTargetWithMinimumRange(TurretBase base,
                                                   World worldObj, int downLowAmount, int xCoord, int yCoord,
                                                   int zCoord, int turretRange, TurretHead turret) {

        Entity target = null;

        if (!worldObj.isRemote && base != null && base.getOwner() != null) {
            AxisAlignedBB axis = AxisAlignedBB.getBoundingBox(xCoord
                            - turretRange, yCoord - downLowAmount,
                    zCoord - turretRange, xCoord + turretRange, yCoord
                            + turretRange, zCoord + turretRange);

            List<EntityLivingBase> targets = worldObj.getEntitiesWithinAABB(
                    EntityLivingBase.class, axis);

            for (EntityLivingBase target1 : targets) {

                if (base.isAttacksNeutrals() && ConfigHandler.globalCanTargetNeutrals) {
                    if (target1 instanceof EntityAnimal && !target1.isDead
                            && target1.getDistance(xCoord, yCoord, zCoord) >= 3) {
                        target = target1;
                    }
                }

                if (base.isAttacksNeutrals() && ConfigHandler.globalCanTargetNeutrals) {
                    if (target1 instanceof EntityAmbientCreature
                            && !target1.isDead
                            && target1.getDistance(xCoord, yCoord, zCoord) >= 3) {
                        target = target1;
                    }
                }

                if (base.isAttacksMobs() && ConfigHandler.globalCanTargetMobs) {
                    if (target1 instanceof IMob && !target1.isDead
                            && target1.getDistance(xCoord, yCoord, zCoord) >= 3) {
                        target = target1;
                    }
                }

                if (base.isAttacksPlayers() && ConfigHandler.globalCanTargetMobs) {
                    if (target1 instanceof EntityPlayerMP && !target1.isDead
                            && target1.getDistance(xCoord, yCoord, zCoord) >= 3) {
                        EntityPlayerMP entity = (EntityPlayerMP) target1;

                        if (!entity.getUniqueID().toString().equals(base.getOwner())
                                && !isTrustedPlayer(entity.getUniqueID(),
                                base) && !entity.capabilities.isCreativeMode) {
                            target = target1;
                        }
                    }
                }

                if (target != null && turret != null) {

                    EntityLivingBase targetELB = (EntityLivingBase) target;

                    if (canTurretSeeTarget(turret, targetELB)
                            && targetELB.getHealth() > 0.0F) {
                        return target;
                    }
                }
            }
        }
        return null;
    }

    public static Entity getTargetWithoutSlowEffect(TurretBase base,
                                                    World worldObj, int downLowAmount, int xCoord, int yCoord,
                                                    int zCoord, int turretRange, TurretHead turret) {

        Entity target = null;

        if (!worldObj.isRemote && base != null && base.getOwner() != null) {
            AxisAlignedBB axis = AxisAlignedBB.getBoundingBox(xCoord
                            - turretRange, yCoord - downLowAmount,
                    zCoord - turretRange, xCoord + turretRange, yCoord
                            + turretRange, zCoord + turretRange);

            List<EntityLivingBase> targets = worldObj.getEntitiesWithinAABB(
                    EntityLivingBase.class, axis);

            for (EntityLivingBase target1 : targets) {

                if (base.isAttacksNeutrals() && ConfigHandler.globalCanTargetNeutrals) {
                    if (target1 instanceof EntityAnimal && !target1.isDead
                            && !target1.isPotionActive(Potion.moveSlowdown.id)) {
                        target = target1;
                    }
                }

                if (base.isAttacksNeutrals() && ConfigHandler.globalCanTargetNeutrals) {
                    if (target1 instanceof EntityAmbientCreature
                            && !target1.isDead
                            && !target1.isPotionActive(Potion.moveSlowdown.id)) {
                        target = target1;
                    }
                }

                if (base.isAttacksMobs() && ConfigHandler.globalCanTargetMobs) {
                    if (target1 instanceof IMob && !target1.isDead
                            && !target1.isPotionActive(Potion.moveSlowdown.id)) {
                        target = target1;
                    }
                }

                if (base.isAttacksPlayers() && ConfigHandler.globalCanTargetPlayers) {
                    if (target1 instanceof EntityPlayerMP && !target1.isDead
                            && !target1.isPotionActive(Potion.moveSlowdown.id)) {
                        EntityPlayerMP entity = (EntityPlayerMP) target1;

                        if (!entity.getUniqueID().toString().equals(base.getOwner())
                                && !isTrustedPlayer(entity.getUniqueID(),
                                base) && !entity.capabilities.isCreativeMode) {
                            target = target1;
                        }
                    }
                }

                if (target != null && turret != null) {

                    EntityLivingBase targetELB = (EntityLivingBase) target;

                    if (canTurretSeeTarget(turret, targetELB)
                            && targetELB.getHealth() > 0.0F) {
                        return target;
                    }
                }
            }
        }
        return null;
    }

    public static boolean isTrustedPlayer(UUID uuid, TurretBase base) {
        for (TrustedPlayer trusted_player : base.getTrustedPlayers()) {
            if (trusted_player.uuid.equals(uuid)) {
                return true;
            }
        }

        return false;
    }

    public static TurretBase getTurretBase(World world, int x, int y, int z) {
        if (world == null) {
            return null;
        }

        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
            int offsetX = x + direction.offsetX;
            int offsetY = y + direction.offsetY;
            int offsetZ = z + direction.offsetZ;

            if (world.getTileEntity(offsetX, offsetY, offsetZ) instanceof TurretBase) {
                return (TurretBase) world.getTileEntity(offsetX, offsetY,
                        offsetZ);
            }
        }

        return null;
    }

    public static float getAimYaw(Entity target, int xCoord, int yCoord,
                                  int zCoord) {
        double dX = (target.posX) - (xCoord);
        double dZ = (target.posZ) - (zCoord);
        float yaw = (float) Math.atan2(dZ, dX);
        yaw = yaw - 1.570796F + 3.1F;
        return yaw;
    }

    public static float getAimPitch(Entity target, int xCoord, int yCoord,
                                    int zCoord) {
        double dX = (target.posX - 0.2F) - (xCoord + 0.6F);
        double dY = (target.posY + 0.6F) - (yCoord - 0.6F);
        double dZ = (target.posZ - 0.2F) - (zCoord + 0.6F);
        float pitch = (float) (Math.atan2(Math.sqrt(dZ * dZ + dX * dX), dY) + Math.PI);
        pitch = pitch + 1.65F;
        return pitch;
    }

    public static ItemStack useAnyItemStackFromBase(TurretBase base) {
        for (int i = 0; i <= 8; i++) {
            ItemStack ammoCheck = base.getStackInSlot(i);
            if (ammoCheck != null && ammoCheck.stackSize > 0
                    && ammoCheck.getItem() != null) {
                base.decrStackSize(i, 1);
                return new ItemStack(ammoCheck.getItem());
            }
        }

        return null;
    }

    public static ItemStack useSpecificItemStackBlockFromBase(TurretBase base, ItemStack stack) {
        for (int i = 0; i <= 8; i++) {
            ItemStack ammo_stack = base.getStackInSlot(i);

            if (ammo_stack != null && ammo_stack.stackSize > 0
                    && ammo_stack.getItem() == stack.getItem()) {
                base.decrStackSize(i, 1);
                return new ItemStack(ammo_stack.getItem());
            }
        }

        return null;
    }

    public static ItemStack useSpecificItemStackItemFromBase(TurretBase base,
                                                             Item item) {
        for (int i = 0; i <= 8; i++) {
            ItemStack ammo_stack = base.getStackInSlot(i);

            if (ammo_stack != null && ammo_stack.stackSize > 0
                    && ammo_stack.getItem() == item) {
                base.decrStackSize(i, 1);
                return new ItemStack(ammo_stack.getItem());
            }
        }

        return null;
    }

    public static int getRangeUpgrades(TurretBase base) {
        int value = 0;
        int tier = base.getBaseTier();

        if (tier == 1) {
            return value;
        }

        if (tier == 5) {
            if (base.getStackInSlot(12) != null) {
                if (base.getStackInSlot(12).getItem() instanceof RangeUpgradeItem) {
                    value += (ConfigHandler.getRangeUpgradeBoost() * base
                            .getStackInSlot(12).stackSize);
                }
            }
        }

        if (base.getStackInSlot(11) != null) {
            if (base.getStackInSlot(11).getItem() instanceof RangeUpgradeItem) {
                value += (ConfigHandler.getRangeUpgradeBoost() * base
                        .getStackInSlot(11).stackSize);
            }
        }

        return value;
    }

    public static int getScattershotUpgrades(TurretBase base) {
        int value = 0;
        int tier = base.getBaseTier();

        if (tier == 1) {
            return value;
        }

        if (tier == 5) {
            if (base.getStackInSlot(12) != null) {
                if (base.getStackInSlot(12).getItem() instanceof ScattershotUpgradeItem) {
                    value += (ConfigHandler.getRangeUpgradeBoost() * base
                            .getStackInSlot(12).stackSize);
                }
            }
        }

        if (base.getStackInSlot(11) != null) {
            if (base.getStackInSlot(11).getItem() instanceof ScattershotUpgradeItem) {
                value += (ConfigHandler.getRangeUpgradeBoost() * base
                        .getStackInSlot(11).stackSize);
            }
        }

        return value;
    }

    public static float getAccuraccyUpgrades(TurretBase base) {
        float accuracy = 0.0F;
        int tier = base.getBaseTier();

        if (tier == 1) {
            return accuracy;
        }

        if (tier == 5) {
            if (base.getStackInSlot(12) != null) {
                if (base.getStackInSlot(12).getItem() instanceof AccuraccyUpgradeItem) {
                    accuracy += (ConfigHandler.getAccuracyUpgradeBoost() * base
                            .getStackInSlot(12).stackSize);
                }
            }
        }

        if (base.getStackInSlot(11) != null) {
            if (base.getStackInSlot(11).getItem() instanceof AccuraccyUpgradeItem) {
                accuracy += (ConfigHandler.getAccuracyUpgradeBoost() * base
                        .getStackInSlot(11).stackSize);
            }
        }

        return accuracy;
    }

    public static float getEfficiencyUpgrades(TurretBase base) {
        float efficiency = 0.0F;
        int tier = base.getBaseTier();

        if (tier == 1) {
            return efficiency;
        }

        if (tier == 5) {
            if (base.getStackInSlot(12) != null) {
                if (base.getStackInSlot(12).getItem() instanceof EfficiencyUpgradeItem) {
                    efficiency += (ConfigHandler
                            .getEfficiencyUpgradeBoostPercentage() * base
                            .getStackInSlot(12).stackSize);
                }
            }
        }

        if (base.getStackInSlot(11) != null) {
            if (base.getStackInSlot(11).getItem() instanceof EfficiencyUpgradeItem) {
                efficiency += (ConfigHandler
                        .getEfficiencyUpgradeBoostPercentage() * base
                        .getStackInSlot(11).stackSize);
            }
        }

        return efficiency;
    }

    public static float getFireRateUpgrades(TurretBase base) {
        float rof = 0.0F;
        int tier = base.getBaseTier();

        if (tier == 1) {
            return rof;
        }

        if (tier == 5) {
            if (base.getStackInSlot(12) != null) {
                if (base.getStackInSlot(12).getItem() instanceof FireRateUpgradeItem) {
                    rof += (ConfigHandler.getFireRateUpgradeBoostPercentage() * base
                            .getStackInSlot(12).stackSize);
                }
            }
        }

        if (base.getStackInSlot(11) != null) {
            if (base.getStackInSlot(11).getItem() instanceof FireRateUpgradeItem) {
                rof += (ConfigHandler.getFireRateUpgradeBoostPercentage() * base
                        .getStackInSlot(11).stackSize);
            }
        }

        return rof;
    }

    public static boolean hasRedstoneReactor(TurretBase base) {
        boolean found = false;
        if (base instanceof TurretBaseTierOneTileEntity) {
            return false;
        }

        if (base.getStackInSlot(9) != null) {
            found = base.getStackInSlot(9).getItem() instanceof RedstoneReactorAddonItem;
        }

        if (base.getStackInSlot(10) != null && !found) {
            found = base.getStackInSlot(10).getItem() instanceof RedstoneReactorAddonItem;
        }
        return found;
    }

    public static boolean hasDamageAmpAddon(TurretBase base) {
        boolean found = false;
        if (base instanceof TurretBaseTierOneTileEntity) {
            return false;
        }

        if (base.getStackInSlot(9) != null) {
            found = base.getStackInSlot(9).getItem() instanceof DamageAmpAddonItem;
        }

        if (base.getStackInSlot(10) != null && !found) {
            found = base.getStackInSlot(10).getItem() instanceof DamageAmpAddonItem;
        }
        return found;
    }

    public static boolean hasSolarPanelAddon(TurretBase base) {
        boolean found = false;
        if (base instanceof TurretBaseTierOneTileEntity) {
            return false;
        }

        if (base.getStackInSlot(9) != null) {
            found = base.getStackInSlot(9).getItem() instanceof SolarPanelAddonItem;
        }

        if (base.getStackInSlot(10) != null && !found) {
            found = base.getStackInSlot(10).getItem() instanceof SolarPanelAddonItem;
        }
        return found;
    }

    public static boolean hasPotentiaUpgradeAddon(TurretBase base) {
        boolean found = false;
        if (base instanceof TurretBaseTierOneTileEntity) {
            return false;
        }
        if (!ModCompatibility.ThaumcraftLoaded) {
            return false;
        }

        if (base.getStackInSlot(9) != null) {
            found = base.getStackInSlot(9).getItem() instanceof PotentiaAddonItem;
        }

        if (base.getStackInSlot(10) != null && !found) {
            found = base.getStackInSlot(10).getItem() instanceof PotentiaAddonItem;
        }
        return found;
    }

    public static boolean hasSerialPortAddon(TurretBase base) {
        boolean found = false;
        if (base instanceof TurretBaseTierOneTileEntity) {
            return false;
        }
        if (!ModCompatibility.OpenComputersLoaded || !ModCompatibility.ComputercraftLoaded) {
            return false;
        }

        if (base.getStackInSlot(9) != null) {
            found = base.getStackInSlot(9).getItem() instanceof SerialPortAddonItem;
        }

        if (base.getStackInSlot(10) != null && !found) {
            found = base.getStackInSlot(10).getItem() instanceof SerialPortAddonItem;
        }
        return found;
    }

    public static int getAmpLevel(TurretBase base) {
        int amp_level = 0;
        int tier = base.getBaseTier();

        if (tier == 1) {
            return amp_level;
        }

        if (base.getStackInSlot(10) != null) {
            if (base.getStackInSlot(10).getItem() instanceof DamageAmpAddonItem) {
                amp_level += (ConfigHandler.getDamageAmpDmgBonus() * base
                        .getStackInSlot(10).stackSize);
            }
        }

        if (base.getStackInSlot(9) != null) {
            if (base.getStackInSlot(9).getItem() instanceof DamageAmpAddonItem) {
                amp_level += (ConfigHandler.getDamageAmpDmgBonus() * base
                        .getStackInSlot(9).stackSize);
            }
        }

        return amp_level;
    }

    public static void updateSolarPanelAddon(TurretBase base) {
        if (!hasSolarPanelAddon(base)) {
            return;
        }

        if (base.getWorldObj().isDaytime()
                && !base.getWorldObj().isRaining()
                && base.getWorldObj().canBlockSeeTheSky(base.xCoord,
                base.yCoord + 2, base.zCoord)) {
            base.receiveEnergy(ForgeDirection.UNKNOWN,
                    ConfigHandler.getSolarPanelAddonGen(), false);
        }
    }

    public static void updateRedstoneReactor(TurretBase base) {
        if (!hasRedstoneReactor(base)) {
            return;
        }

        if (ConfigHandler.getRedstoneReactorAddonGen() < (base
                .getMaxEnergyStored(ForgeDirection.UNKNOWN) - base
                .getEnergyStored(ForgeDirection.UNKNOWN))) {

            //Prioritise redstone blocks
            ItemStack redstoneBlock = useSpecificItemStackBlockFromBase(base,
                    new ItemStack(Blocks.redstone_block));

            if (redstoneBlock != null) {
                base.receiveEnergy(ForgeDirection.UNKNOWN,
                        ConfigHandler.getRedstoneReactorAddonGen() * 9, false);
                return;
            }

            ItemStack redstone = useSpecificItemStackItemFromBase(base,
                    Items.redstone);

            if (redstone != null) {
                base.receiveEnergy(ForgeDirection.UNKNOWN,
                        ConfigHandler.getRedstoneReactorAddonGen(), false);
            }
        }
    }

    public static boolean canTurretSeeTarget(TurretHead turret,
                                             EntityLivingBase target) {

        Vec3 traceStart = Vec3.createVectorHelper(turret.xCoord + 0.5F,
                turret.yCoord + 0.5F, turret.zCoord + 0.5F);
        Vec3 traceEnd = Vec3.createVectorHelper(target.posX, target.posY
                + target.getEyeHeight(), target.posZ);
        Vec3 vecDelta = Vec3.createVectorHelper(traceEnd.xCoord
                        - traceStart.xCoord, traceEnd.yCoord - traceStart.yCoord,
                traceEnd.zCoord - traceStart.zCoord);

        // Normalize vector to the largest delta axis
        double vecDeltaLength = MathHelper.abs_max(vecDelta.xCoord,
                MathHelper.abs_max(vecDelta.yCoord, vecDelta.zCoord));
        vecDelta.xCoord /= vecDeltaLength;
        vecDelta.yCoord /= vecDeltaLength;
        vecDelta.zCoord /= vecDeltaLength;

        // Limit how many non solid block a turret can see through
        for (int i = 0; i < 10; i++) {
            // Offset start position toward the target to prevent self collision
            traceStart.xCoord += vecDelta.xCoord;
            traceStart.yCoord += vecDelta.yCoord;
            traceStart.zCoord += vecDelta.zCoord;

            MovingObjectPosition traced = turret.getWorldObj().rayTraceBlocks(
                    Vec3.createVectorHelper(traceStart.xCoord,
                            traceStart.yCoord, traceStart.zCoord),
                    Vec3.createVectorHelper(traceEnd.xCoord, traceEnd.yCoord,
                            traceEnd.zCoord));

            if (traced != null && traced.typeOfHit == traced.typeOfHit.BLOCK) {
                Block hitBlock = turret.getWorldObj().getBlock(traced.blockX,
                        traced.blockY, traced.blockZ);

                // If non solid block is in the way then proceed to continue
                // tracing
                if (hitBlock != null
                        && !hitBlock.getMaterial().isSolid()
                        && MathHelper.abs_max(
                        MathHelper.abs_max(traceStart.xCoord
                                - traceEnd.xCoord, traceStart.yCoord
                                - traceEnd.yCoord), traceStart.zCoord
                                - traceEnd.zCoord) > 1) {
                    // Start at new position and continue
                    traceStart = traced.hitVec;
                    continue;
                }
            }

            EntityLivingBase targeted = target != null && traced == null ? target
                    : null;

            if (targeted != null && targeted.equals(target)) {
                return true;
            } else {
                return false;
            }
        }

        // If all above failed, the target cannot be seen
        return false;
    }
}
