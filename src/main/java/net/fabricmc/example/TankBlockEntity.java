package net.fabricmc.example;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;

import static net.fabricmc.example.ExampleMod.TANK_BLOCK_ENTITY;

public class TankBlockEntity extends BlockEntity  {
    public final SingleVariantStorage<FluidVariant> fluid;

    public TankBlockEntity(BlockPos pos, BlockState state) {
        super(TANK_BLOCK_ENTITY, pos, state);

        this.fluid = new TankFluidStorage(pos, state) {
            @Override
            public void onFinalCommit() {
                super.onFinalCommit();
                markDirty();
            }
        };
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        tag.put("fluidVariant", fluid.variant.toNbt());
        tag.putLong("amount", fluid.amount);

        super.writeNbt(tag);
    }
    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);

        fluid.variant = FluidVariant.fromNbt(tag.getCompound("fluidVariant"));
        fluid.amount = tag.getLong("amount");
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    abstract class TankFluidStorage extends SingleVariantStorage<FluidVariant> {
        private final BlockPos pos;
        private final BlockState state;

        public TankFluidStorage(BlockPos pos, BlockState state) {
            this.pos = pos;
            this.state = state;
        }

        @Override
        protected FluidVariant getBlankVariant() {
            return FluidVariant.blank();
        }

        @Override
        protected long getCapacity(FluidVariant variant) {
            return 8 * FluidConstants.BUCKET;
        }

        @Override
        public void onFinalCommit() {
            world.updateListeners(this.pos, this.state, this.state, Block.NOTIFY_LISTENERS);
        }
    }
}