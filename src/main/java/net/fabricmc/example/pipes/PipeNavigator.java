package net.fabricmc.example.pipes;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.nio.channels.Pipe;
import java.util.*;
import java.util.function.Function;

public class PipeNavigator {
    private record PipeCoords(BlockPos start, BlockPos dest) { }
    private static Map<PipeCoords, BlockPos> nextPipe = new HashMap<>();

    public static BlockPos findNextPipe(BlockPos start, BlockPos dest, World world) {
        var coords = new PipeCoords(start, dest);

        if (nextPipe.containsKey(coords)) {
            return nextPipe.get(coords);
        }

        findDestination(start, world, pos -> pos.equals(dest));

        if (nextPipe.containsKey(coords)) {
            return nextPipe.get(coords);
        }
        else {
            return null;
        }
    }

    public static BlockPos findDestination(BlockPos start, World world) {
        return findDestination(start, world, pos -> {
            var entity = world.getBlockEntity(pos);
            if (entity == null) {
                return false;
            }
            else if (entity instanceof PipeBlockEntity || !(entity instanceof Inventory)) {
                return false;
            }
            else {
                return true;
            }
        });
    }

    private static BlockPos findDestination(BlockPos start, World world, Function<BlockPos, Boolean> isDest) {
        var visited = new HashSet<BlockPos>();
        var parents = new HashMap<BlockPos, BlockPos>();
        var dest = new BlockPos(0, 0, 0);

        var toVisit = (Queue<BlockPos>)new LinkedList<BlockPos>();
        toVisit.add(start);

        // Loop through the pipe network backwards, and at each step store the next pipe we should visit if we
        // want to get to our destination. This traversal is breadth-first so it will always yield the shortest
        // path.
        while (!toVisit.isEmpty()) {
            var next = toVisit.remove();

            // If we found our destination, stop the traversal
            if (isDest.apply(next)) {
                dest = next;
                break;
            }

            // Mark this pipe as visited
            if (visited.contains(next)) {
                continue;
            }
            else {
                visited.add(next);
            }

            // Traverse all the pipes this pipe is attached to
            for (var pos : getAttachedPipesOrInventories(next, world)) {
                if (!visited.contains(pos)) {
                    toVisit.add(pos);
                    parents.put(pos, next);
                }
            }
        }

        var current = dest;

        // Now backtrack through all of the nodes and add map that keeps track of which path to take
        while (current != start) {
            var parent = parents.get(current);
            var coords = new PipeCoords(parent, dest);

            nextPipe.put(coords, current);
            current = parent;
        }

        return dest;
    }

    private static Iterable<BlockPos> getAttachedPipesOrInventories(BlockPos pipePos, World world) {
        var result = new ArrayList<BlockPos>();

        // Find connected blocks
        for (var dir : Direction.values()) {
            var target = world.getBlockEntity(pipePos.offset(dir));
            if (target == null) {
                continue;
            }

            if (target instanceof PipeBlockEntity) {
                var prop = PipeBlock.PROP_MAP.get(dir);

                // We are not connected in this direction
                if (!world.getBlockState(pipePos).get(prop).booleanValue()) {
                    continue;
                }
            }
            else if (target instanceof Inventory inv) {
                // If this is an extractor pipe, possibly ignore the connection
                if (world.getBlockEntity(pipePos) instanceof ExtractorPipeBlockEntity) {
                    if (world.getBlockState(pipePos).get(ExtractorBlock.FACING) == dir) {
                        continue;
                    }
                }
            }

            result.add(pipePos.offset(dir));
        }

        return result;
    }
}
