package joshuakearney.practical_pipes.features.pipes;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class PipeBlockEntity extends BlockEntity {
    private final ArrayList<PipeResource> resources = new ArrayList<>();
    private final ArrayList<PipeResource> resourcesToKeep = new ArrayList<>();

    public PipeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public boolean canItemPassThrough(Direction dir) {
        var state = this.getCachedState().get(PipeBlock.getConnectionProperty(dir));

        return state != PipeConnection.None;
    }

    public Iterable<PipeResource> getResources() { return this.resources; }

    public boolean hasResources() { return !this.resources.isEmpty(); }

    public void addResource(BlockPos previousPos, PipeResource resource) {
        resource.ticksInPipe = 0;

        // Calculate the new from and to directions
        var from = Direction.fromVector(this.getPos().subtract(previousPos)).getOpposite();

        // Get the next pipe to move to if we are going to reach our destination
        var nextPos = PipeNavigator.findNextPipe(this.getPos(), resource.destination, this.getWorld());
        if (nextPos == null) {
            // TODO: Drop the item
            return;
        }

        var to = Direction.fromVector(this.getPos().subtract(nextPos)).getOpposite();

        resource.fromDirection = from;
        resource.toDirection = to;

        this.resources.add(resource);
        this.markDirty();
    }

    public void tickServer() {
        if (this.resources.isEmpty()) {
            return;
        }

        var toRemove = new ArrayList<PipeResource>();

        for (var resource : this.resources) {
            resource.ticksInPipe++;

            if (resource.ticksInPipe < resource.ticksPerPipe) {
                continue;
            }

            // Get the next pipe to move to if we are going to reach our destination
            var nextPos = PipeNavigator.findNextPipe(this.getPos(), resource.destination, this.getWorld());
            if (nextPos == null) {
                // TODO: Drop the item
                continue;
            }

            // Make sure it is a pipe or inventory
            var entity = this.getWorld().getBlockEntity(nextPos);

            if (entity instanceof PipeBlockEntity pipe) {
                // Add this resource to the next pipe and remove it from ours
                toRemove.add(resource);
                pipe.addResource(this.getPos(), resource);
            }
            else if (entity instanceof Inventory dest) {
                toRemove.add(resource);
                this.depositToInventory(resource.items, dest);
            }
        }

        if (!toRemove.isEmpty()) {
            this.resources.removeAll(toRemove);
            this.markDirty();
        }
    }

    @Environment(EnvType.CLIENT)
    public void tickClient() {
        for (var resource : this.resources) {
            resource.ticksInPipe++;
        }
    }

    private void depositToInventory(ItemStack sourceStack, Inventory dest) {
        int destSlot = 0;
        for (; destSlot < dest.size(); destSlot++) {
            var destStack = dest.getStack(destSlot);

            if (destStack.isEmpty()) {
                break;
            }
            else if (destStack.getItem() == sourceStack.getItem() && destStack.getCount() < destStack.getMaxCount()) {
                break;
            }
        }

        if (destSlot >= dest.size()) {
            // TODO: Inventory full, drop item or reroute
            return;
        }

        var destStack = dest.getStack(destSlot);
        if (destStack.isEmpty()) {
            destStack = new ItemStack(sourceStack.getItem(), 1);
        }
        else {
            destStack = destStack.copy();
            destStack.increment(1);
        }

        dest.setStack(destSlot, destStack);
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        tag.putInt("resource_size", this.resources.size());

        int counter = 0;
        for (var resource : this.resources) {
            tag.put("resource_" + counter, resource.toNbt());
            counter++;
        }

        super.writeNbt(tag);
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        this.resourcesToKeep.clear();

        int size = tag.getInt("resource_size");
        for (int i = 0; i < size; i++) {
            var resourceTag = tag.getCompound("resource_" + i);
            var resource = PipeResource.fromNbt(resourceTag);

            var existing = this.tryFindResource(
                    resource.source,
                    resource.destination,
                    resource.createdOnTicks);

            if (existing == null) {
                this.resources.add(resource);
            }
            else {
                existing.copyFrom(resource);
                this.resourcesToKeep.add(existing);
            }
        }

        for (int i = this.resources.size() - 1; i >= 0; i--) {
            var resource = this.resources.get(i);

            if (!this.resourcesToKeep.contains(resource)) {
                this.resources.remove(i);
            }
        }
    }

    @Nullable
    private PipeResource tryFindResource(BlockPos source, BlockPos dest, long createdOn) {
        for (var resource : this.resources) {
            var isMatch = resource.createdOnTicks == createdOn
                    && resource.source.equals(source)
                    && resource.destination.equals(dest);

            if (isMatch) {
                return resource;
            }
        }

        return null;
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this, e -> e.createNbt());
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return this.createNbt();
    }

    @Override
    public void markDirty() {
        super.markDirty();
        world.updateListeners(this.pos, this.getCachedState(), this.getCachedState(), Block.NOTIFY_ALL);
    }
}