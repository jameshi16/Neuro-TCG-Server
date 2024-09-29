package objects

import objects.packets.*

class Game(val p1Connection: GameConnection, val p2connection: GameConnection, db: GameDatabase) {
    private val boardManager = BoardStateManager(db, p1Connection, p2connection)

    val id = boardManager.gameID

    suspend fun mainLoop(player: Player) {
        val prefix = "[Game ${id}][Player ${if (player == Player.Player1) 1 else 2}] "
        val connection = if (player == Player.Player1) p1Connection else p2connection
        val otherConnection = if (player == Player.Player1) p2connection else p1Connection

        println(prefix + "Starting game")
        println(prefix + "Sending game rules to client")
        connection.sendPacket(RuleInfoPacket())
        println(prefix + "Sending match to client")
        connection.sendPacket(MatchFoundPacket(otherConnection.getUserInfo(), id, false, player == Player.Player1))

        if (player == Player.Player1)
            connection.sendPacket(StartTurnPacket())
        while (connection.isOpen) {
            boardManager.withGameOverHandler {
                when (val packet = connection.receivePacket()) {
                    null -> {
                        if (connection.isOpen)
                            connection.close()
                        println(prefix + "Connection was closed unexpectedly")

                        if (otherConnection.isOpen) {
                            println(prefix + "Informing opponent")
                            otherConnection.sendPacket(
                                DisconnectPacket(
                                    DisconnectPacket.Reason.opponent_disconnect,
                                    "The opponent has closed it's connection"
                                )
                            )
                            //otherConnection.close()
                        }
                    }

                    is GetBoardStatePacket -> {
                        println(prefix + "getboardstate")
                        connection.sendPacket(GetBoardStateResponse(boardManager.getBoardState()))
                    }

                    is AttackRequestPacket -> {
                        boardManager.handleAttackPacket(packet, player)
                    }

                    is SummonRequestPacket -> {
                        boardManager.handleSummonPacket(packet, player)
                    }

                    is SwitchPlaceRequestPacket -> {
                        boardManager.handleSwitchPlacePacket(packet, player)
                    }

                    is EndTurnPacket -> {
                        boardManager.handleEndTurn(player)
                    }

                    is DrawCardRequestPacket -> {
                        boardManager.handleDrawCard(player)
                    }

                    is UseAbilityRequestPacket -> {
                        boardManager.handleUseAbilityPacket(packet, player)
                    }

                    else -> {
                        connection.sendPacket(UnknownPacketPacket("unknown packet type received"))
                        println(prefix + "Received unknown packet")
                    }
                }
            }
            println(prefix + "new ram: ${boardManager.getBoardState().ram[0]}, ${boardManager.getBoardState().ram[1]}  max: ${boardManager.getBoardState().max_ram[0]}, ${boardManager.getBoardState().max_ram[1]}")
        }
    }
}
