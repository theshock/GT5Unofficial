package gregtech.common.items.behaviors;

import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Utility;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import ic2.core.block.BlockWall;
import appeng.block.networking.BlockCableBus;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Behaviour_Spray_Solvent
        extends Behaviour_None {
    private final ItemStack mEmpty;
    private final ItemStack mUsed;
    private final ItemStack mFull;
    private final long mUses;
    private final byte mColor;
    private final Collection<Block> mAllowedVanillaBlocks = Arrays.asList(new Block[]{Blocks.stained_glass, Blocks.stained_glass_pane, Blocks.carpet, Blocks.hardened_clay, ItemList.TE_Rockwool.getBlock()});
    private final String mTooltip;
    private final String mTooltipUses = GT_LanguageManager.addStringLocalization("gt.behaviour.paintspray.uses", "Remaining Uses:");
    private final String mTooltipUnstackable = GT_LanguageManager.addStringLocalization("gt.behaviour.unstackable", "Not usable when stacked!");

    public Behaviour_Spray_Solvent(ItemStack aEmpty, ItemStack aUsed, ItemStack aFull, long aUses) {
        this.mEmpty = aEmpty;
        this.mUsed = aUsed;
        this.mFull = aFull;
        this.mUses = aUses;
        this.mColor = -1;
        this.mTooltip = GT_LanguageManager.addStringLocalization("gt.behaviour.paintspray." + this.mColor + ".tooltip", "Can wash off the paint");
    }

    public boolean onItemUseFirst(GT_MetaBase_Item aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int aSide, float hitX, float hitY, float hitZ) {
        if ((aWorld.isRemote) || (aStack.stackSize != 1)) {
            return false;
        }
        boolean rOutput = false;
        if (!aPlayer.canPlayerEdit(aX, aY, aZ, aSide, aStack)) {
            return false;
        }
        NBTTagCompound tNBT = aStack.getTagCompound();
        if (tNBT == null) {
            tNBT = new NBTTagCompound();
        }
        long tUses = tNBT.getLong("GT.RemainingPaint");
        if (GT_Utility.areStacksEqual(aStack, this.mFull, true)) {
            aStack.func_150996_a(this.mUsed.getItem());
            Items.feather.setDamage(aStack, Items.feather.getDamage(this.mUsed));
            tUses = this.mUses;
        }
        if ((GT_Utility.areStacksEqual(aStack, this.mUsed, true)) && (colorize(aWorld, aX, aY, aZ, aSide, aPlayer))) {
            GT_Utility.sendSoundToPlayers(aWorld, (String) GregTech_API.sSoundList.get(Integer.valueOf(102)), 1.0F, 1.0F, aX, aY, aZ);
            if (!aPlayer.capabilities.isCreativeMode) {
                tUses -= 1L;
            }
            rOutput = true;
        }
        tNBT.removeTag("GT.RemainingPaint");
        if (tUses > 0L) {
            tNBT.setLong("GT.RemainingPaint", tUses);
        }
        if (tNBT.hasNoTags()) {
            aStack.setTagCompound(null);
        } else {
            aStack.setTagCompound(tNBT);
        }
        if (tUses <= 0L) {
            if (this.mEmpty == null) {
                aStack.stackSize -= 1;
            } else {
                aStack.func_150996_a(this.mEmpty.getItem());
                Items.feather.setDamage(aStack, Items.feather.getDamage(this.mEmpty));
            }
        }
        return rOutput;
    }

    public boolean colorize(World aWorld, int aX, int aY, int aZ, int aSide, EntityPlayer p) {
        Block aBlock = aWorld.getBlock(aX, aY, aZ);
        if ((aBlock != Blocks.air) && ((this.mAllowedVanillaBlocks.contains(aBlock)) || ((aBlock instanceof BlockColored)))) {
            if (aBlock == Blocks.stained_hardened_clay) {
                aWorld.setBlock(aX, aY, aZ, Blocks.hardened_clay);
                return true;
            }
            if (aBlock == Blocks.stained_glass_pane) {
                aWorld.setBlock(aX, aY, aZ, Blocks.glass_pane);
                return true;
            }
            if (aBlock == Blocks.stained_glass) {
                aWorld.setBlock(aX, aY, aZ, Blocks.glass);
                return true;
            }
            if (aWorld.getBlockMetadata(aX, aY, aZ) == ((this.mColor ^ 0xFFFFFFFF) & 0xF)) {
                return false;
            }
            aWorld.setBlockMetadataWithNotify(aX, aY, aZ, (this.mColor ^ 0xFFFFFFFF) & 0xF, 3);
            return true;
        }
        TileEntity tTileEntity = aWorld.getTileEntity(aX,aY,aZ);
        if (tTileEntity instanceof IGregTechTileEntity) {
            IGregTechTileEntity iGregTechTileEntity = (IGregTechTileEntity) tTileEntity;
            if(iGregTechTileEntity.getColorization()!=-1) {
                iGregTechTileEntity.setColorization((byte) -1);
                return true;
            } else return false;
        }
        if (aBlock instanceof BlockWall) {
            if (aWorld.getBlockMetadata(aX, aY, aZ) != 7) {
                aBlock.recolourBlock(aWorld, aX, aY, aZ, ForgeDirection.getOrientation(aSide), 8);
                return true;
            } else return false;
        }
        if (aBlock instanceof BlockCableBus) {
            final ForgeDirection orientation = ForgeDirection.getOrientation(aSide);
            return ((BlockCableBus) aBlock).recolourBlock(aWorld, aX, aY, aZ, orientation, 16, p);
        }		
        return aBlock.recolourBlock(aWorld, aX, aY, aZ, ForgeDirection.getOrientation(aSide), (this.mColor ^ 0xFFFFFFFF) & 0xF);
    }

    public List<String> getAdditionalToolTips(GT_MetaBase_Item aItem, List<String> aList, ItemStack aStack) {
        aList.add(this.mTooltip);
        NBTTagCompound tNBT = aStack.getTagCompound();
        long tRemainingPaint = tNBT == null ? 0L : GT_Utility.areStacksEqual(aStack, this.mFull, true) ? this.mUses : tNBT.getLong("GT.RemainingPaint");
        aList.add(this.mTooltipUses + " " + tRemainingPaint);
        aList.add(this.mTooltipUnstackable);
        return aList;
    }
}
