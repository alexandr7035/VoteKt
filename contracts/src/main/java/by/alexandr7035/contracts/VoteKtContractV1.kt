package by.alexandr7035.contracts

import java.math.BigInteger
import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import pm.gnosis.model.Solidity
import pm.gnosis.model.SolidityBase
import pm.gnosis.utils.BigIntegerUtils

// AUTO-GENERATED code!!!
// TODO add gradle plugin
public class VoteKtContractV1 {
    public object CreateProposal {
        public const val METHOD_ID: String = "4c15676b"

        public fun encode(
            uuid: Solidity.String,
            title: Solidity.String,
            description: Solidity.String,
            durationInDays: Solidity.UInt256
        ): String = "0x" + METHOD_ID + pm.gnosis.model.SolidityBase.encodeFunctionArguments(uuid,
                title, description, durationInDays)

        public fun decodeArguments(`data`: String): Arguments {
            val source = SolidityBase.PartitionData.of(data)

            // Add decoders
            val arg0Offset = BigIntegerUtils.exact(BigInteger(source.consume(), 16))
            val arg0 = Solidity.String.DECODER.decode(source.subData(arg0Offset))
            val arg1Offset = BigIntegerUtils.exact(BigInteger(source.consume(), 16))
            val arg1 = Solidity.String.DECODER.decode(source.subData(arg1Offset))
            val arg2Offset = BigIntegerUtils.exact(BigInteger(source.consume(), 16))
            val arg2 = Solidity.String.DECODER.decode(source.subData(arg2Offset))
            val arg3 = Solidity.UInt256.DECODER.decode(source)

            return Arguments(arg0, arg1, arg2, arg3)
        }

        public data class Arguments(
            public val uuid: Solidity.String,
            public val title: Solidity.String,
            public val description: Solidity.String,
            public val durationindays: Solidity.UInt256
        )
    }

    public object DeleteProposal {
        public const val METHOD_ID: String = "8259d553"

        public fun encode(proposalNumber: Solidity.UInt256): String =
                "0x" + METHOD_ID + pm.gnosis.model.SolidityBase.encodeFunctionArguments(proposalNumber)

        public fun decodeArguments(`data`: String): Arguments {
            val source = SolidityBase.PartitionData.of(data)

            // Add decoders
            val arg0 = Solidity.UInt256.DECODER.decode(source)

            return Arguments(arg0)
        }

        public data class Arguments(
            public val proposalnumber: Solidity.UInt256
        )
    }

    public object GetProposalDetails {
        public const val METHOD_ID: String = "3b4d01a7"

        public fun encode(proposalNumber: Solidity.UInt256): String =
                "0x" + METHOD_ID + pm.gnosis.model.SolidityBase.encodeFunctionArguments(proposalNumber)

        public fun decode(`data`: String): Return {
            val source = SolidityBase.PartitionData.of(data)

            // Add decoders
            val arg0Offset = BigIntegerUtils.exact(BigInteger(source.consume(), 16))
            val arg0 = TupleA.DECODER.decode(source.subData(arg0Offset))

            return Return(arg0)
        }

        public fun decodeArguments(`data`: String): Arguments {
            val source = SolidityBase.PartitionData.of(data)

            // Add decoders
            val arg0 = Solidity.UInt256.DECODER.decode(source)

            return Arguments(arg0)
        }

        public data class Return(
            public val param0: TupleA
        )

        public data class Arguments(
            public val proposalnumber: Solidity.UInt256
        )
    }

    public object GetProposalsList {
        public const val METHOD_ID: String = "578178c0"

        public fun encode(): String = "0x" + METHOD_ID

        public fun decode(`data`: String): Return {
            val source = SolidityBase.PartitionData.of(data)

            // Add decoders
            val arg0Offset = BigIntegerUtils.exact(BigInteger(source.consume(), 16))
            val arg0 = SolidityBase.Vector.Decoder(TupleA.DECODER).decode(source.subData(arg0Offset))

            return Return(arg0)
        }

        public data class Return(
            public val param0: SolidityBase.Vector<TupleA>
        )
    }

    public object HasVoted {
        public const val METHOD_ID: String = "43859632"

        public fun encode(arg1: Solidity.UInt256, arg2: Solidity.Address): String =
                "0x" + METHOD_ID + pm.gnosis.model.SolidityBase.encodeFunctionArguments(arg1, arg2)

        public fun decode(`data`: String): Return {
            val source = SolidityBase.PartitionData.of(data)

            // Add decoders
            val arg0 = Solidity.Bool.DECODER.decode(source)

            return Return(arg0)
        }

        public fun decodeArguments(`data`: String): Arguments {
            val source = SolidityBase.PartitionData.of(data)

            // Add decoders
            val arg0 = Solidity.UInt256.DECODER.decode(source)
            val arg1 = Solidity.Address.DECODER.decode(source)

            return Arguments(arg0, arg1)
        }

        public data class Return(
            public val param0: Solidity.Bool
        )

        public data class Arguments(
            public val param0: Solidity.UInt256,
            public val param1: Solidity.Address
        )
    }

    public object MaxProposals {
        public const val METHOD_ID: String = "2f3fe24c"

        public fun encode(): String = "0x" + METHOD_ID

        public fun decode(`data`: String): Return {
            val source = SolidityBase.PartitionData.of(data)

            // Add decoders
            val arg0 = Solidity.UInt256.DECODER.decode(source)

            return Return(arg0)
        }

        public data class Return(
            public val param0: Solidity.UInt256
        )
    }

    public object Owner {
        public const val METHOD_ID: String = "8da5cb5b"

        public fun encode(): String = "0x" + METHOD_ID

        public fun decode(`data`: String): Return {
            val source = SolidityBase.PartitionData.of(data)

            // Add decoders
            val arg0 = Solidity.Address.DECODER.decode(source)

            return Return(arg0)
        }

        public data class Return(
            public val param0: Solidity.Address
        )
    }

    public object Proposals {
        public const val METHOD_ID: String = "013cf08b"

        public fun encode(arg1: Solidity.UInt256): String =
                "0x" + METHOD_ID + pm.gnosis.model.SolidityBase.encodeFunctionArguments(arg1)

        public fun decode(`data`: String): Return {
            val source = SolidityBase.PartitionData.of(data)

            // Add decoders
            val arg0 = Solidity.UInt256.DECODER.decode(source)
            val arg1Offset = BigIntegerUtils.exact(BigInteger(source.consume(), 16))
            val arg1 = Solidity.String.DECODER.decode(source.subData(arg1Offset))
            val arg2Offset = BigIntegerUtils.exact(BigInteger(source.consume(), 16))
            val arg2 = Solidity.String.DECODER.decode(source.subData(arg2Offset))
            val arg3Offset = BigIntegerUtils.exact(BigInteger(source.consume(), 16))
            val arg3 = Solidity.String.DECODER.decode(source.subData(arg3Offset))
            val arg4 = Solidity.UInt256.DECODER.decode(source)
            val arg5 = Solidity.UInt256.DECODER.decode(source)
            val arg6 = Solidity.UInt256.DECODER.decode(source)
            val arg7 = Solidity.UInt256.DECODER.decode(source)

            return Return(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7)
        }

        public fun decodeArguments(`data`: String): Arguments {
            val source = SolidityBase.PartitionData.of(data)

            // Add decoders
            val arg0 = Solidity.UInt256.DECODER.decode(source)

            return Arguments(arg0)
        }

        public data class Return(
            public val number: Solidity.UInt256,
            public val uuid: Solidity.String,
            public val title: Solidity.String,
            public val description: Solidity.String,
            public val votesfor: Solidity.UInt256,
            public val votesagainst: Solidity.UInt256,
            public val creationtime: Solidity.UInt256,
            public val expirationtime: Solidity.UInt256
        )

        public data class Arguments(
            public val param0: Solidity.UInt256
        )
    }

    public object RenounceOwnership {
        public const val METHOD_ID: String = "715018a6"

        public fun encode(): String = "0x" + METHOD_ID
    }

    public object TransferOwnership {
        public const val METHOD_ID: String = "f2fde38b"

        public fun encode(newOwner: Solidity.Address): String =
                "0x" + METHOD_ID + pm.gnosis.model.SolidityBase.encodeFunctionArguments(newOwner)

        public fun decodeArguments(`data`: String): Arguments {
            val source = SolidityBase.PartitionData.of(data)

            // Add decoders
            val arg0 = Solidity.Address.DECODER.decode(source)

            return Arguments(arg0)
        }

        public data class Arguments(
            public val newowner: Solidity.Address
        )
    }

    public object Vote {
        public const val METHOD_ID: String = "c9d27afe"

        public fun encode(proposalNumber: Solidity.UInt256, inFavor: Solidity.Bool): String =
                "0x" + METHOD_ID + pm.gnosis.model.SolidityBase.encodeFunctionArguments(proposalNumber,
                inFavor)

        public fun decodeArguments(`data`: String): Arguments {
            val source = SolidityBase.PartitionData.of(data)

            // Add decoders
            val arg0 = Solidity.UInt256.DECODER.decode(source)
            val arg1 = Solidity.Bool.DECODER.decode(source)

            return Arguments(arg0, arg1)
        }

        public data class Arguments(
            public val proposalnumber: Solidity.UInt256,
            public val infavor: Solidity.Bool
        )
    }

    public object Events {
        public object VoteCasted {
            public const val EVENT_ID: String =
                    "98b03f1463128a74cc9dd4acc43b54ef12ac07daacbcd621d2e4266091b7024a"

            public fun decode(topics: List<String>, `data`: String): Arguments {
                // Decode topics
                if (topics.first().removePrefix("0x") != EVENT_ID) throw IllegalArgumentException("topics[0] does not match event id")

                // Decode data
                val source = SolidityBase.PartitionData.of(data)
                val arg0 = Solidity.UInt256.DECODER.decode(source)
                val arg1 = Solidity.Bool.DECODER.decode(source)
                return Arguments(arg0, arg1)
            }

            public data class Arguments(
                public val proposalnumber: Solidity.UInt256,
                public val infavor: Solidity.Bool
            )
        }
    }

    public data class TupleA(
        public val number: Solidity.UInt256,
        public val uuid: Solidity.String,
        public val title: Solidity.String,
        public val description: Solidity.String,
        public val votesfor: Solidity.UInt256,
        public val votesagainst: Solidity.UInt256,
        public val creationtime: Solidity.UInt256,
        public val expirationtime: Solidity.UInt256
    ) : SolidityBase.DynamicType {
        public override fun encode(): String = SolidityBase.encodeFunctionArguments(number, uuid,
                title, description, votesfor, votesagainst, creationtime, expirationtime)

        public override fun encodePacked(): String = throw UnsupportedOperationException("Structs are  not supported via encodePacked")

        public class Decoder : SolidityBase.TypeDecoder<TupleA> {
            public override fun isDynamic(): Boolean = true

            public override fun decode(source: SolidityBase.PartitionData): TupleA {
                val arg0 = Solidity.UInt256.DECODER.decode(source)
                val arg1Offset = BigIntegerUtils.exact(BigInteger(source.consume(), 16))
                val arg1 = Solidity.String.DECODER.decode(source.subData(arg1Offset))
                val arg2Offset = BigIntegerUtils.exact(BigInteger(source.consume(), 16))
                val arg2 = Solidity.String.DECODER.decode(source.subData(arg2Offset))
                val arg3Offset = BigIntegerUtils.exact(BigInteger(source.consume(), 16))
                val arg3 = Solidity.String.DECODER.decode(source.subData(arg3Offset))
                val arg4 = Solidity.UInt256.DECODER.decode(source)
                val arg5 = Solidity.UInt256.DECODER.decode(source)
                val arg6 = Solidity.UInt256.DECODER.decode(source)
                val arg7 = Solidity.UInt256.DECODER.decode(source)
                return TupleA(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7)
            }
        }

        public companion object {
            public val DECODER: Decoder = Decoder()
        }
    }
}