package net.fabricmc.example;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;

import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.impl.SimpleFixedFluidInv;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;

import static net.fabricmc.example.ExampleMod.TANK_BLOCK_ENTITY;

public class TankBlockEntity extends BlockEntity  {
    public static final FluidAmount TANK_CAPACITY = FluidAmount.BUCKET.mul(8);

    public final SimpleFixedFluidInv fluidInventory;

    public TankBlockEntity(BlockPos pos, BlockState state) {
        super(TANK_BLOCK_ENTITY, pos, state);

        this.fluidInventory = new SimpleFixedFluidInv(1, TANK_CAPACITY);
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);

        var fluid = this.fluidInventory.getInvFluid(0);
        if (!fluid.isEmpty()) {
            tag.put("fluid", fluid.toTag());
        }
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);

        if (tag.contains("fluid")) {
            var fluid = FluidVolume.fromTag(tag.getCompound("fluid"));
            this.fluidInventory.setInvFluid(0, fluid, Simulation.ACTION);
        }
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }
}