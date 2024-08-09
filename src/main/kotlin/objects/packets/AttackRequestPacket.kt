package objects.packets

import kotlinx.serialization.*
import objects.packets.objects.*

@Serializable
// https://github.com/Kotlin/kotlinx.serialization/blob/master/docs/polymorphism.md
@SerialName(PacketType.ATTACK_REQUEST)
@Suppress("PropertyName")
/**
 * Sent by: Client
 *
 * @param target_position the card of the opponent to attack
 * @param attacker_position the card of the player that does the attack
 */
class AttackRequestPacket(
    @Required val target_position: CardPosition,
    @Required val attacker_position: CardPosition,
    @Required val counterattack: Boolean
) : Packet() {

    fun getResponsePacket(isYou: Boolean, valid: Boolean, targetCard: CardState?, attackerCard: CardState?, counterattack: Boolean): AttackPacket {
        return AttackPacket(isYou, valid, target_position, attacker_position, targetCard, attackerCard, counterattack)
    }

}

@Serializable
@SerialName(PacketType.ATTACK)
@Suppress("PropertyName")
/**
 *
 * Sent by: Server
 *
 * Informs the client of an attack by either it or the opponent.
 *
 * If any of the cards were killed by this attack, they will be set to `null`.
 */
class AttackPacket(
    @Required val is_you: Boolean,
    @Required val valid: Boolean,
    @Required val target_position: CardPosition?,
    @Required val attacker_position: CardPosition?,
    @Required val target_card: CardState?,
    @Required val attacker_card: CardState?,
    @Required val counterattack: Boolean,
) : Packet()
