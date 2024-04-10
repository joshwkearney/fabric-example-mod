package joshuakearney.practical_pipes.features.pipes;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Unmodifiable;

public class PipeResource {
    public ItemStack items = ItemStack.EMPTY;

    public BlockPos destination = null;

    public BlockPos source = null;

    public long createdOnTicks = 0;

    public Direction fromDirection = null;

    public Direction toDirection = null;

    public int ticksPerPipe = 0;

    public int ticksInPipe = 0;

    public Vec3d clientRenderLocation = null;

    public PipeResource(ItemStack items, int ticksPerPipe, int ticksInPipe, BlockPos source, BlockPos dest, long createdOn, Direction fromDir, Direction toDir) {
        this.items = items;
        this.ticksPerPipe = ticksPerPipe;
        this.ticksInPipe = ticksInPipe;
        this.source = source;
        this.destination = dest;
        this.createdOnTicks = createdOn;
        this.fromDirection = fromDir;
        this.toDirection = toDir;
    }

    public static PipeResource fromNbt(NbtCompound tag) {
        var items = ItemStack.EMPTY;
        var ticksPerPipe = 0;
        var ticksInPipe = 0;
        var source = new BlockPos(0, 0, 0);
        var dest = new BlockPos(0, 0, 0);
        var fromDir = Direction.NORTH;
        var toDir = Direction.SOUTH;
        var createdOn = 0L;

        if (tag.contains("items")) {
            items = ItemStack.fromNbt(tag.getCompound("items"));
        }

        if (tag.contains("ticksPerPipe")) {
            ticksPerPipe = tag.getInt("ticksPerPipe");
        }

        if (tag.contains("ticksInPipe")) {
            ticksInPipe = tag.getInt("ticksInPipe");
        }

        if (tag.contains("source")) {
            source = NbtHelper.toBlockPos(tag.getCompound("source"));
        }

        if (tag.contains("destination")) {
            dest = NbtHelper.toBlockPos(tag.getCompound("destination"));
        }

        if (tag.contains("fromDirection")) {
            fromDir = Direction.byId(tag.getInt("fromDirection"));
        }

        if (tag.contains("toDirection")) {
            toDir = Direction.byId(tag.getInt("toDirection"));
        }

        if (tag.contains("createdOnTicks")) {
            createdOn = tag.getLong("createdOnTicks");
        }

        return new PipeResource(items, ticksPerPipe, ticksInPipe, source, dest, createdOn, fromDir, toDir);
    }

    public NbtCompound toNbt() {
        var tag = new NbtCompound();
        var itemsTag = new NbtCompound();

        this.items.writeNbt(itemsTag);
        tag.put("items", itemsTag);
        tag.putInt("ticksPerPipe", this.ticksPerPipe);
        tag.putInt("ticksInPipe", this.ticksInPipe);
        tag.put("source", NbtHelper.fromBlockPos(this.source));
        tag.put("destination", NbtHelper.fromBlockPos(this.destination));
        tag.putInt("fromDirection", this.fromDirection.getId());
        tag.putInt("toDirection", this.toDirection.getId());
        tag.putLong("createdOnTicks", this.createdOnTicks);

        return tag;
    }

    public void copyFrom(PipeResource other) {
        this.items = other.items;
        this.ticksPerPipe = other.ticksPerPipe;
        this.ticksInPipe = other.ticksInPipe;
        this.source = other.source;
        this.destination = other.destination;
        this.fromDirection = other.fromDirection;
        this.toDirection = other.toDirection;
        this.createdOnTicks = other.createdOnTicks;
    }
}