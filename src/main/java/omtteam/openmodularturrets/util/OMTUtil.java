package omtteam.openmodularturrets.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import omtteam.openmodularturrets.handler.OMTConfigHandler;
import omtteam.openmodularturrets.items.AmmoMetaItem;

import java.util.Set;

import static omtteam.omlib.util.InvUtil.getStackSize;

/**
 * Created by Keridos on 06/02/17.
 * This Class
 */
public class OMTUtil {
    public static boolean isItemStackValidAmmo(ItemStack itemStack) {
        if (itemStack == ItemStack.EMPTY) return false;
        if (!OMTConfigHandler.useWhitelistForAmmo || itemStack.getItem() == Items.POTATO
                || itemStack.getItem() == Items.REDSTONE || itemStack.getItem() == Item.getItemFromBlock(Blocks.REDSTONE_BLOCK))
            return true;
        for (ItemStack itemStackAllowed : OMTConfigHandler.disposableAmmoList) {
            if (itemStackAllowed.getItem() == itemStack.getItem() && (getStackSize(itemStackAllowed) == 2 ||
                    itemStackAllowed.getItemDamage() == itemStack.getItemDamage())) {
                return true;
            }
        }
        return itemStack.getItem() instanceof AmmoMetaItem;
    }

    public static int getFakeDropsLevel(EntityLivingBase entity) {
        Set<String> tags = entity.getTags();
        return (tags.contains("openmodularturrets:fake_drops_0") ? 0 : tags.contains("openmodularturrets:fake_drops_1") ? 1 :
                tags.contains("openmodularturrets:fake_drops_2") ? 2 : tags.contains("openmodularturrets:fake_drops_3") ? 3 : -1);
    }
}
