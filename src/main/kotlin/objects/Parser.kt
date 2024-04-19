package objects

class Parser {
    fun parse(clientMessage: String): ClientCommand {
        val clientMessageArray: List<String> = clientMessage.split(",")
        val clientCommand: String = clientMessageArray[0]

        return when (clientCommand) {
            "exit" -> ClientCommand(ClientCommandType.Exit)
            "ping" -> ClientCommand(ClientCommandType.Ping)
            "cards" -> ClientCommand(ClientCommandType.Cards)
            "attack" -> {
                val packet = ClientCommand(ClientCommandType.Attack)
                packet.row = clientMessageArray[1].toInt()
                packet.column = clientMessageArray[2].toInt()
                packet.targetRow = clientMessageArray[3].toInt()
                packet.targetColumn = clientMessageArray[4].toInt()
                packet
            }

            "summon" -> {
                val packet = ClientCommand(ClientCommandType.Summon)
                packet.row = clientMessageArray[1].toInt()
                packet.column = clientMessageArray[2].toInt()
                packet.cardIndex = clientMessageArray[3].toInt()
                packet
            }

            "gameEvent" -> {
                val packet = ClientCommand(ClientCommandType.GameEvent)
                val clientMessageMutable = clientMessageArray.toMutableList()
                clientMessageMutable.removeFirst()
                packet.message = clientMessageMutable.joinToString(",")
                packet
            }

            "version" -> {
                val version = ClientCommand(ClientCommandType.Version)
                if (clientMessageArray.size < 2) {
                    version.message = "invalid version packet"
                } else {
                    version.message = clientMessageArray[1]
                }
                version
            }

            else -> {
                val msg = ClientCommand(ClientCommandType.Message)
                msg.message = clientMessageArray.joinToString(",")
                msg
            }
        }
    }
}
