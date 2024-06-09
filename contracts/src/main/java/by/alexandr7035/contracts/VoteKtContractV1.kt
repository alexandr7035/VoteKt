package `by`.alexandr7035.contracts

import pm.gnosis.model.Solidity
import pm.gnosis.model.SolidityBase
import pm.gnosis.utils.BigIntegerUtils
import java.math.BigInteger
import kotlin.Boolean
import kotlin.String
import kotlin.collections.List

public class VoteKtContractV1 {
    public object CREATE_PROPOSAL_FEE {
        public const val METHOD_ID: String = "fef60742"

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

    public object MAX_PROPOSAL_COUNT {
        public const val METHOD_ID: String = "dfe9ec58"

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

    public object MAX_PROPOSAL_DESCRIPTION_LENGTH {
        public const val METHOD_ID: String = "037d20f5"

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

    public object MAX_PROPOSAL_DURATION {
        public const val METHOD_ID: String = "bd968216"

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

    public object MAX_PROPOSAL_TITLE_LENGTH {
        public const val METHOD_ID: String = "670a1c7e"

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

    public object NIN_PROPOSAL_DURATION {
        public const val METHOD_ID: String = "111da574"

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

    public object CreateProposal {
        public const val METHOD_ID: String = "4c15676b"

        public fun encode(
            uuid: Solidity.String,
            title: Solidity.String,
            description: Solidity.String,
            durationInHours: Solidity.UInt256
        ): String = "0x" + METHOD_ID + pm.gnosis.model.SolidityBase.encodeFunctionArguments(
            uuid, title, description, durationInHours
        )

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
            public val durationinhours: Solidity.UInt256
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

        public fun encode(arg1: Solidity.UInt256): String = "0x" + METHOD_ID + pm.gnosis.model.SolidityBase.encodeFunctionArguments(arg1)

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
            val arg6 = Solidity.Address.DECODER.decode(source)
            val arg7 = Solidity.UInt256.DECODER.decode(source)
            val arg8 = Solidity.UInt256.DECODER.decode(source)

            return Return(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8)
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
            public val creatoraddress: Solidity.Address,
            public val creationtimemills: Solidity.UInt256,
            public val expirationtimemills: Solidity.UInt256
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
            "0x" + METHOD_ID + pm.gnosis.model.SolidityBase.encodeFunctionArguments(
                proposalNumber, inFavor
            )

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
        public object OwnershipTransferred {
            public const val EVENT_ID: String = "8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e0"

            public fun decode(topics: List<String>): Arguments {
                // Decode topics
                if (topics.first().removePrefix("0x") != EVENT_ID) {
                    throw IllegalArgumentException(
                        "topics[0] does not match event id"
                    )
                }
                val source1 = SolidityBase.PartitionData.of(topics[1])
                val t1 = Solidity.Address.DECODER.decode(source1)
                val source2 = SolidityBase.PartitionData.of(topics[2])
                val t2 = Solidity.Address.DECODER.decode(source2)
                return Arguments(t1, t2)
            }

            public data class Arguments(
                public val previousowner: Solidity.Address,
                public val newowner: Solidity.Address
            )
        }

        public object ProposalCreated {
            public const val EVENT_ID: String = "9c770c289ab5bf7e57cb1d23c8ceae993aea46eb64847072fd3d78ca60d3e432"

            public fun decode(topics: List<String>, `data`: String): Arguments {
                // Decode topics
                if (topics.first().removePrefix("0x") != EVENT_ID) {
                    throw IllegalArgumentException(
                        "topics[0] does not match event id"
                    )
                }

                // Decode data
                val source = SolidityBase.PartitionData.of(data)
                val arg0 = Solidity.UInt256.DECODER.decode(source)
                val arg1Offset = BigIntegerUtils.exact(BigInteger(source.consume(), 16))
                val arg1 = Solidity.String.DECODER.decode(source.subData(arg1Offset))
                return Arguments(arg0, arg1)
            }

            public data class Arguments(
                public val proposalnumber: Solidity.UInt256,
                public val title: Solidity.String
            )
        }

        public object ProposalDeleted {
            public const val EVENT_ID: String = "61c0d93dc2b610877e420b107c8d12e9185e46e04a505da758cc7f7329ae545f"

            public fun decode(topics: List<String>, `data`: String): Arguments {
                // Decode topics
                if (topics.first().removePrefix("0x") != EVENT_ID) {
                    throw IllegalArgumentException(
                        "topics[0] does not match event id"
                    )
                }

                // Decode data
                val source = SolidityBase.PartitionData.of(data)
                val arg0 = Solidity.UInt256.DECODER.decode(source)
                return Arguments(arg0)
            }

            public data class Arguments(
                public val proposalnumber: Solidity.UInt256
            )
        }

        public object VoteCasted {
            public const val EVENT_ID: String = "98b03f1463128a74cc9dd4acc43b54ef12ac07daacbcd621d2e4266091b7024a"

            public fun decode(topics: List<String>, `data`: String): Arguments {
                // Decode topics
                if (topics.first().removePrefix("0x") != EVENT_ID) {
                    throw IllegalArgumentException(
                        "topics[0] does not match event id"
                    )
                }

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
        public val creatoraddress: Solidity.Address,
        public val creationtimemills: Solidity.UInt256,
        public val expirationtimemills: Solidity.UInt256
    ) : SolidityBase.DynamicType {
        public override fun encode(): String = SolidityBase.encodeFunctionArguments(
            number, uuid, title, description, votesfor, votesagainst, creatoraddress, creationtimemills, expirationtimemills
        )

        public override fun encodePacked(): String = throw UnsupportedOperationException(
            "Structs are  not supported via encodePacked"
        )

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
                val arg6 = Solidity.Address.DECODER.decode(source)
                val arg7 = Solidity.UInt256.DECODER.decode(source)
                val arg8 = Solidity.UInt256.DECODER.decode(source)
                return TupleA(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8)
            }
        }

        public companion object {
            public val DECODER: Decoder = Decoder()
        }
    }
}
