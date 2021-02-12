package com.fuzs.sneakymagic.network.message;

import com.fuzs.puzzleslib_sm.network.message.IMessage;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;

public class SAnvilRepairMessage implements IMessage {

    private BlockPos blockPos;
    private int stateId;

    public SAnvilRepairMessage() {

    }

    public SAnvilRepairMessage(BlockPos blockPos, BlockState blockState) {

        this.blockPos = blockPos;
        this.stateId = Block.getStateId(blockState);
    }

    @Override
    public void writePacketData(PacketBuffer buf) {

        buf.writeBlockPos(this.blockPos);
        buf.writeInt(this.stateId);
    }

    @Override
    public void readPacketData(PacketBuffer buf) {

        this.blockPos = buf.readBlockPos();
        this.stateId = buf.readInt();
    }

    @Override
    public void processPacket(PlayerEntity player) {

        // play repair sound
        player.world.playEvent(Constants.WorldEvents.ANVIL_USE_SOUND, this.blockPos, 0);

        // show block breaking particles for anvil
        BlockState blockstate = Block.getStateById(this.stateId);
        IMessage.getMinecraft().particles.addBlockDestroyEffects(this.blockPos, blockstate);
    }

}
