package idealindustrial.tile.interfaces.host;

import gregtech.api.interfaces.IFastRenderedTileEntity;
import gregtech.api.interfaces.tileentity.IHasWorldObjectAndCoords;
import idealindustrial.tile.IOType;
import idealindustrial.tile.covers.BaseCoverBehavior;
import idealindustrial.tile.host.WorldAction;
import idealindustrial.tile.interfaces.IClickableTileEntity;
import idealindustrial.tile.interfaces.ISyncedTileEntity;
import idealindustrial.tile.interfaces.meta.Tile;
import idealindustrial.tile.interfaces.IUpdatableTileEntity;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.function.Consumer;

public interface HostTile extends IUpdatableTileEntity, IHasWorldObjectAndCoords, IFastRenderedTileEntity, ISyncedTileEntity, IClickableTileEntity {
    int getMetaTileID();
    void setMetaTileID(int id);
    Tile<?> getMetaTile();

    ArrayList<ItemStack> getDrops();

    void syncTileEntity();

    void sendEvent(int id, int value);
    void issueTextureUpdate();

    void onPlaced();
    void onWorldStateUpdated(Consumer<WorldAction> listener);

    void receiveNeighbourIOConfigChange(IOType type);

    boolean isAllowedToWork();
    void setAllowedToWork(boolean allow);
    boolean isActive();
    void setActive(boolean active);

    int getCoverIDAtSide(int side);
    BaseCoverBehavior<?> getCoverAtSide(int side);
    long getCoverVarAtSide(int side);

    void dropCoverAtSide(int side);
    void setCoverVarAtSide(int side, long value);
}
