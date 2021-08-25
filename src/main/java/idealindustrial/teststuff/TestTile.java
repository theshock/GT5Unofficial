package idealindustrial.teststuff;

import gregtech.api.interfaces.IIconContainer;
import idealindustrial.autogen.material.II_Materials;
import idealindustrial.autogen.material.Prefixes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TestTile extends TileEntity {
    static {
        addMapping(TileEntity.class, "testTileII");
    }
    public TestTile(World world) {
        worldObj = world;
    }

    public IIconContainer getTexture() {
        return II_Materials.iron.getSolidRenderInfo().getTextureSet().mTextures[Prefixes.block.textureIndex];
    }
}
