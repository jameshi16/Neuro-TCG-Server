package objects.packets

import kotlinx.serialization.*
import objects.packets.objects.*

@Serializable
@SerialName(PacketType.GET_BOARD_STATE)
/**
 * Sent by: Client
 */
class GetBoardStatePacket(
    @Required val reason: Reason
) : Packet() {


    @Suppress("EnumEntryName")
    enum class Reason {
        /**
         * The client cannot apply some state update due to a conflict
         */
        state_conflict,

        /**
         * The client has reconnected to an existing game
         */
        reconnect,

        /**
         * The client has connected to a new game
         */
        connect,

        /**
         * The client wants to get the current state for debugging purposes, this includes proactively checking if the
         * current state matches what the server has
         */
        debug,
    }
}

@Serializable
@SerialName(PacketType.GET_BOARD_STATE_RESPONSE)
/*
 * Sent by: Server
 *
 * Contains the full game state. The client should validate that this matches its own state and/or replace it.
 */
class GetBoardStateResponse(
    @Required val board: BoardState
) : Packet()
