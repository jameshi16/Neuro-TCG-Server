package objects

class Parser {
    fun parse(clientMessage: String): ClientCommand {
        val clientMessageArray: List<String> = clientMessage.split(",")
        val clientCommand: String = clientMessageArray[0]

        return when (clientCommand) {
            "exit" -> ClientCommand(ClientCommandType.Exit)
            "ping" -> ClientCommand(ClientCommandType.Ping)

            else -> {
                val msg = ClientCommand(ClientCommandType.Message)
                msg.message = clientMessageArray.joinToString(",")
                msg
            }
        }
    }
}
