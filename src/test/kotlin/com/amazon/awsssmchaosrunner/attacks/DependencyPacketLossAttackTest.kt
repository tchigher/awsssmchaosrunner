package com.amazon.awsssmchaosrunner.attacks

import com.amazon.awsssmchaosrunner.attacks.SSMAttack.Companion.getAttack
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Collections

class DependencyPacketLossAttackTest {
    @RelaxedMockK
    lateinit var ssm: AWSSimpleSystemsManagement

    lateinit var attack: SSMAttack

    @BeforeEach
    fun prepTest() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        attack = getAttack(
                ssm,
                SSMAttack.Companion.AttackConfiguration(
                        name = "DependencyPacketLossAttack",
                        duration = "PT10M",
                        timeoutSeconds = 120,
                        cloudWatchLogGroupName = "",
                        targets = Collections.emptyList(),
                        concurrencyPercentage = 100,
                        otherParameters = mutableMapOf("dependencyEndpoint" to "test-endpoint",
                                "packetLossPercentage" to "100", "dependencyPort" to "1234")
                )
        )
    }

    @Test
    fun `when getAttack called documentContent contains required parameters`() {
        assertTrue(attack.documentContent.contains("test-endpoint"))
        assertTrue(attack.documentContent.contains("100"))
        assertTrue(attack.documentContent.contains("1234"))
    }
}