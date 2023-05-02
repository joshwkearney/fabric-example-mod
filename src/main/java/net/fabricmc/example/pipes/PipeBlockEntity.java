package net.fabricmc.example.pipes;

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

import java.util.*;

import static net.fabricmc.example.ExampleMod.PIPE_BLOCK_ENTITY;

public class PipeBlockEntity extends BlockEntity {
    private static final int MOVE_TIME = 20;

    public final Set<PipeResource> resources = new HashSet<>();

    public PipeBlockEntity(BlockPos pos, BlockState state) {
        super(PIPE_BLOCK_ENTITY, pos, state);
    }

    public PipeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void tick() {
        if (this.resources.isEmpty()) {
            return;
        }

        var toRemove = new HashSet<PipeResource>();

        for (var resource : this.resources) {
            resource.ticksLeftInPipe--;

            if (resource.ticksLeftInPipe > 0) {
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
                // Reset the traversal time for the new pipe
                resource.ticksLeftInPipe = MOVE_TIME;

                // Add this resource to the next pipe and remove it from ours
                toRemove.add(resource);
                pipe.resources.add(resource);
                pipe.markDirty();
            }
            else if (entity instanceof Inventory dest) {
                toRemove.add(resource);
                this.deposit(resource.items, dest);
            }
        }

        if (!toRemove.isEmpty()) {
            this.resources.removeAll(toRemove);
            this.markDirty();
        }
    }

    private void deposit(ItemStack sourceStack, Inventory dest) {
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
        this.resources.clear();

        int size = tag.getInt("resource_size");
        for (int i = 0; i < size; i++) {
            var resourceTag = tag.getCompound("resource_" + i);

            this.resources.add(PipeResource.fromNbt(resourceTag));
        }
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        var result = BlockEntityUpdateS2CPacket.create(this, e -> e.createNbt());

        return result;
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