package idealindustrial.tile.gui.base.component;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class II_SlotOutput extends II_Slot {
    public II_SlotOutput(IInventory inventory, int id, int x, int y, int texture) {
        super(inventory, id, x, y, texture);
    }

    @Override
    public boolean isItemValid(ItemStack p_75214_1_) {
        return super.isItemValid(p_75214_1_);
    }
}
