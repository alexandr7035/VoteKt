package by.alexandr7035.web3j_contracts;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple8;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.4.2.
 */
@SuppressWarnings("rawtypes")
public class VotingContract extends Contract {
    public static final String BINARY = "6080604052600a600355348015610014575f80fd5b5061003161002661003660201b60201c565b61003d60201b60201c565b6100fe565b5f33905090565b5f805f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff169050815f806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508173ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e060405160405180910390a35050565b612017806200010c5f395ff3fe608060405234801561000f575f80fd5b50600436106100a7575f3560e01c8063578178c01161006f578063578178c01461017c578063715018a61461019a5780638259d553146101a45780638da5cb5b146101c0578063c9d27afe146101de578063f2fde38b146101fa576100a7565b8063013cf08b146100ab5780632f3fe24c146100e25780633b4d01a71461010057806343859632146101305780634c15676b14610160575b5f80fd5b6100c560048036038101906100c0919061109a565b610216565b6040516100d998979695949392919061115e565b60405180910390f35b6100ea6103fb565b6040516100f791906111ef565b60405180910390f35b61011a6004803603810190610115919061109a565b610401565b604051610127919061131a565b60405180910390f35b61014a60048036038101906101459190611394565b61061d565b60405161015791906113ec565b60405180910390f35b61017a60048036038101906101759190611531565b610647565b005b61018461087e565b604051610191919061175f565b60405180910390f35b6101a2610abc565b005b6101be60048036038101906101b9919061109a565b610acf565b005b6101c8610bca565b6040516101d5919061178e565b60405180910390f35b6101f860048036038101906101f391906117d1565b610bf1565b005b610214600480360381019061020f919061180f565b610df6565b005b60018181548110610225575f80fd5b905f5260205f2090600802015f91509050805f01549080600101805461024a90611867565b80601f016020809104026020016040519081016040528092919081815260200182805461027690611867565b80156102c15780601f10610298576101008083540402835291602001916102c1565b820191905f5260205f20905b8154815290600101906020018083116102a457829003601f168201915b5050505050908060020180546102d690611867565b80601f016020809104026020016040519081016040528092919081815260200182805461030290611867565b801561034d5780601f106103245761010080835404028352916020019161034d565b820191905f5260205f20905b81548152906001019060200180831161033057829003601f168201915b50505050509080600301805461036290611867565b80601f016020809104026020016040519081016040528092919081815260200182805461038e90611867565b80156103d95780601f106103b0576101008083540402835291602001916103d9565b820191905f5260205f20905b8154815290600101906020018083116103bc57829003601f168201915b5050505050908060040154908060050154908060060154908060070154905088565b60035481565b610409610fbe565b6001828154811061041d5761041c611897565b5b905f5260205f209060080201604051806101000160405290815f820154815260200160018201805461044e90611867565b80601f016020809104026020016040519081016040528092919081815260200182805461047a90611867565b80156104c55780601f1061049c576101008083540402835291602001916104c5565b820191905f5260205f20905b8154815290600101906020018083116104a857829003601f168201915b505050505081526020016002820180546104de90611867565b80601f016020809104026020016040519081016040528092919081815260200182805461050a90611867565b80156105555780601f1061052c57610100808354040283529160200191610555565b820191905f5260205f20905b81548152906001019060200180831161053857829003601f168201915b5050505050815260200160038201805461056e90611867565b80601f016020809104026020016040519081016040528092919081815260200182805461059a90611867565b80156105e55780601f106105bc576101008083540402835291602001916105e5565b820191905f5260205f20905b8154815290600101906020018083116105c857829003601f168201915b505050505081526020016004820154815260200160058201548152602001600682015481526020016007820154815250509050919050565b6002602052815f5260405f20602052805f5260405f205f915091509054906101000a900460ff1681565b61064f610e78565b60035460018054905010610698576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161068f9061190e565b60405180910390fd5b6004846040516106a89190611966565b90815260200160405180910390205f9054906101000a900460ff1615610703576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016106fa906119ec565b60405180910390fd5b5f6001805490509050610714610fbe565b81815f018181525050848160400181905250838160600181905250858160200181905250428160c00181815250505f62015180846107529190611a37565b4261075d9190611a78565b9050808260e0018181525050600182908060018154018082558091505060019003905f5260205f2090600802015f909190919091505f820151815f015560208201518160010190816107af9190611c48565b5060408201518160020190816107c59190611c48565b5060608201518160030190816107db9190611c48565b506080820151816004015560a0820151816005015560c0820151816006015560e0820151816007015550507f9c770c289ab5bf7e57cb1d23c8ceae993aea46eb64847072fd3d78ca60d3e4328387604051610837929190611d17565b60405180910390a160016004886040516108519190611966565b90815260200160405180910390205f6101000a81548160ff02191690831515021790555050505050505050565b60606001805480602002602001604051908101604052809291908181526020015f905b82821015610ab3578382905f5260205f209060080201604051806101000160405290815f82015481526020016001820180546108dc90611867565b80601f016020809104026020016040519081016040528092919081815260200182805461090890611867565b80156109535780601f1061092a57610100808354040283529160200191610953565b820191905f5260205f20905b81548152906001019060200180831161093657829003601f168201915b5050505050815260200160028201805461096c90611867565b80601f016020809104026020016040519081016040528092919081815260200182805461099890611867565b80156109e35780601f106109ba576101008083540402835291602001916109e3565b820191905f5260205f20905b8154815290600101906020018083116109c657829003601f168201915b505050505081526020016003820180546109fc90611867565b80601f0160208091040260200160405190810160405280929190818152602001828054610a2890611867565b8015610a735780601f10610a4a57610100808354040283529160200191610a73565b820191905f5260205f20905b815481529060010190602001808311610a5657829003601f168201915b50505050508152602001600482015481526020016005820154815260200160068201548152602001600782015481525050815260200190600101906108a1565b50505050905090565b610ac4610e78565b610acd5f610ef6565b565b610ad7610e78565b6001805490508110610b1e576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610b1590611d8f565b60405180910390fd5b60018181548110610b3257610b31611897565b5b905f5260205f2090600802015f8082015f9055600182015f610b549190610ffe565b600282015f610b639190610ffe565b600382015f610b729190610ffe565b600482015f9055600582015f9055600682015f9055600782015f905550507f61c0d93dc2b610877e420b107c8d12e9185e46e04a505da758cc7f7329ae545f81604051610bbf91906111ef565b60405180910390a150565b5f805f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905090565b8160018181548110610c0657610c05611897565b5b905f5260205f209060080201600701544210610c57576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610c4e90611df7565b60405180910390fd5b8260025f8281526020019081526020015f205f3373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f9054906101000a900460ff1615610cf1576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610ce890611e5f565b60405180910390fd5b5f60018581548110610d0657610d05611897565b5b905f5260205f20906008020190508315610d3857806004015f815480929190610d2e90611e7d565b9190505550610d52565b806005015f815480929190610d4c90611e7d565b91905055505b600160025f8781526020019081526020015f205f3373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f6101000a81548160ff0219169083151502179055507f98b03f1463128a74cc9dd4acc43b54ef12ac07daacbcd621d2e4266091b7024a8585604051610de7929190611ec4565b60405180910390a15050505050565b610dfe610e78565b5f73ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff1603610e6c576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610e6390611f5b565b60405180910390fd5b610e7581610ef6565b50565b610e80610fb7565b73ffffffffffffffffffffffffffffffffffffffff16610e9e610bca565b73ffffffffffffffffffffffffffffffffffffffff1614610ef4576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610eeb90611fc3565b60405180910390fd5b565b5f805f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff169050815f806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508173ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e060405160405180910390a35050565b5f33905090565b6040518061010001604052805f81526020016060815260200160608152602001606081526020015f81526020015f81526020015f81526020015f81525090565b50805461100a90611867565b5f825580601f1061101b5750611038565b601f0160209004905f5260205f2090810190611037919061103b565b5b50565b5b80821115611052575f815f90555060010161103c565b5090565b5f604051905090565b5f80fd5b5f80fd5b5f819050919050565b61107981611067565b8114611083575f80fd5b50565b5f8135905061109481611070565b92915050565b5f602082840312156110af576110ae61105f565b5b5f6110bc84828501611086565b91505092915050565b6110ce81611067565b82525050565b5f81519050919050565b5f82825260208201905092915050565b5f5b8381101561110b5780820151818401526020810190506110f0565b5f8484015250505050565b5f601f19601f8301169050919050565b5f611130826110d4565b61113a81856110de565b935061114a8185602086016110ee565b61115381611116565b840191505092915050565b5f610100820190506111725f83018b6110c5565b8181036020830152611184818a611126565b905081810360408301526111988189611126565b905081810360608301526111ac8188611126565b90506111bb60808301876110c5565b6111c860a08301866110c5565b6111d560c08301856110c5565b6111e260e08301846110c5565b9998505050505050505050565b5f6020820190506112025f8301846110c5565b92915050565b61121181611067565b82525050565b5f82825260208201905092915050565b5f611231826110d4565b61123b8185611217565b935061124b8185602086016110ee565b61125481611116565b840191505092915050565b5f61010083015f8301516112755f860182611208565b506020830151848203602086015261128d8282611227565b915050604083015184820360408601526112a78282611227565b915050606083015184820360608601526112c18282611227565b91505060808301516112d66080860182611208565b5060a08301516112e960a0860182611208565b5060c08301516112fc60c0860182611208565b5060e083015161130f60e0860182611208565b508091505092915050565b5f6020820190508181035f830152611332818461125f565b905092915050565b5f73ffffffffffffffffffffffffffffffffffffffff82169050919050565b5f6113638261133a565b9050919050565b61137381611359565b811461137d575f80fd5b50565b5f8135905061138e8161136a565b92915050565b5f80604083850312156113aa576113a961105f565b5b5f6113b785828601611086565b92505060206113c885828601611380565b9150509250929050565b5f8115159050919050565b6113e6816113d2565b82525050565b5f6020820190506113ff5f8301846113dd565b92915050565b5f80fd5b5f80fd5b7f4e487b71000000000000000000000000000000000000000000000000000000005f52604160045260245ffd5b61144382611116565b810181811067ffffffffffffffff821117156114625761146161140d565b5b80604052505050565b5f611474611056565b9050611480828261143a565b919050565b5f67ffffffffffffffff82111561149f5761149e61140d565b5b6114a882611116565b9050602081019050919050565b828183375f83830152505050565b5f6114d56114d084611485565b61146b565b9050828152602081018484840111156114f1576114f0611409565b5b6114fc8482856114b5565b509392505050565b5f82601f83011261151857611517611405565b5b81356115288482602086016114c3565b91505092915050565b5f805f80608085870312156115495761154861105f565b5b5f85013567ffffffffffffffff81111561156657611565611063565b5b61157287828801611504565b945050602085013567ffffffffffffffff81111561159357611592611063565b5b61159f87828801611504565b935050604085013567ffffffffffffffff8111156115c0576115bf611063565b5b6115cc87828801611504565b92505060606115dd87828801611086565b91505092959194509250565b5f81519050919050565b5f82825260208201905092915050565b5f819050602082019050919050565b5f61010083015f8301516116285f860182611208565b50602083015184820360208601526116408282611227565b9150506040830151848203604086015261165a8282611227565b915050606083015184820360608601526116748282611227565b91505060808301516116896080860182611208565b5060a083015161169c60a0860182611208565b5060c08301516116af60c0860182611208565b5060e08301516116c260e0860182611208565b508091505092915050565b5f6116d88383611612565b905092915050565b5f602082019050919050565b5f6116f6826115e9565b61170081856115f3565b93508360208202850161171285611603565b805f5b8581101561174d578484038952815161172e85826116cd565b9450611739836116e0565b925060208a01995050600181019050611715565b50829750879550505050505092915050565b5f6020820190508181035f83015261177781846116ec565b905092915050565b61178881611359565b82525050565b5f6020820190506117a15f83018461177f565b92915050565b6117b0816113d2565b81146117ba575f80fd5b50565b5f813590506117cb816117a7565b92915050565b5f80604083850312156117e7576117e661105f565b5b5f6117f485828601611086565b9250506020611805858286016117bd565b9150509250929050565b5f602082840312156118245761182361105f565b5b5f61183184828501611380565b91505092915050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52602260045260245ffd5b5f600282049050600182168061187e57607f821691505b6020821081036118915761189061183a565b5b50919050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52603260045260245ffd5b7f4d61782070726f706f73616c73207265616368656400000000000000000000005f82015250565b5f6118f86015836110de565b9150611903826118c4565b602082019050919050565b5f6020820190508181035f830152611925816118ec565b9050919050565b5f81905092915050565b5f611940826110d4565b61194a818561192c565b935061195a8185602086016110ee565b80840191505092915050565b5f6119718284611936565b915081905092915050565b7f50726f706f73616c20776974682074686973205555494420616c7265616479205f8201527f6578697374730000000000000000000000000000000000000000000000000000602082015250565b5f6119d66026836110de565b91506119e18261197c565b604082019050919050565b5f6020820190508181035f830152611a03816119ca565b9050919050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52601160045260245ffd5b5f611a4182611067565b9150611a4c83611067565b9250828202611a5a81611067565b91508282048414831517611a7157611a70611a0a565b5b5092915050565b5f611a8282611067565b9150611a8d83611067565b9250828201905080821115611aa557611aa4611a0a565b5b92915050565b5f819050815f5260205f209050919050565b5f6020601f8301049050919050565b5f82821b905092915050565b5f60088302611b077fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff82611acc565b611b118683611acc565b95508019841693508086168417925050509392505050565b5f819050919050565b5f611b4c611b47611b4284611067565b611b29565b611067565b9050919050565b5f819050919050565b611b6583611b32565b611b79611b7182611b53565b848454611ad8565b825550505050565b5f90565b611b8d611b81565b611b98818484611b5c565b505050565b5b81811015611bbb57611bb05f82611b85565b600181019050611b9e565b5050565b601f821115611c0057611bd181611aab565b611bda84611abd565b81016020851015611be9578190505b611bfd611bf585611abd565b830182611b9d565b50505b505050565b5f82821c905092915050565b5f611c205f1984600802611c05565b1980831691505092915050565b5f611c388383611c11565b9150826002028217905092915050565b611c51826110d4565b67ffffffffffffffff811115611c6a57611c6961140d565b5b611c748254611867565b611c7f828285611bbf565b5f60209050601f831160018114611cb0575f8415611c9e578287015190505b611ca88582611c2d565b865550611d0f565b601f198416611cbe86611aab565b5f5b82811015611ce557848901518255600182019150602085019450602081019050611cc0565b86831015611d025784890151611cfe601f891682611c11565b8355505b6001600288020188555050505b505050505050565b5f604082019050611d2a5f8301856110c5565b8181036020830152611d3c8184611126565b90509392505050565b7f496e76616c69642070726f706f73616c206e756d6265720000000000000000005f82015250565b5f611d796017836110de565b9150611d8482611d45565b602082019050919050565b5f6020820190508181035f830152611da681611d6d565b9050919050565b7f566f74696e6720706572696f64206861732065787069726564000000000000005f82015250565b5f611de16019836110de565b9150611dec82611dad565b602082019050919050565b5f6020820190508181035f830152611e0e81611dd5565b9050919050565b7f596f75206861766520616c726561647920766f746564000000000000000000005f82015250565b5f611e496016836110de565b9150611e5482611e15565b602082019050919050565b5f6020820190508181035f830152611e7681611e3d565b9050919050565b5f611e8782611067565b91507fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8203611eb957611eb8611a0a565b5b600182019050919050565b5f604082019050611ed75f8301856110c5565b611ee460208301846113dd565b9392505050565b7f4f776e61626c653a206e6577206f776e657220697320746865207a65726f20615f8201527f6464726573730000000000000000000000000000000000000000000000000000602082015250565b5f611f456026836110de565b9150611f5082611eeb565b604082019050919050565b5f6020820190508181035f830152611f7281611f39565b9050919050565b7f4f776e61626c653a2063616c6c6572206973206e6f7420746865206f776e65725f82015250565b5f611fad6020836110de565b9150611fb882611f79565b602082019050919050565b5f6020820190508181035f830152611fda81611fa1565b905091905056fea264697066735822122057bfd48521365f8e2773b40bfdd7f2fa537c224aa8e351bdf4415c7cb5b703e164736f6c63430008140033";

    public static final String FUNC_CREATEPROPOSAL = "createProposal";

    public static final String FUNC_DELETEPROPOSAL = "deleteProposal";

    public static final String FUNC_GETPROPOSALDETAILS = "getProposalDetails";

    public static final String FUNC_GETPROPOSALSLIST = "getProposalsList";

    public static final String FUNC_HASVOTED = "hasVoted";

    public static final String FUNC_MAXPROPOSALS = "maxProposals";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_PROPOSALS = "proposals";

    public static final String FUNC_RENOUNCEOWNERSHIP = "renounceOwnership";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final String FUNC_VOTE = "vote";

    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event PROPOSALCREATED_EVENT = new Event("ProposalCreated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event PROPOSALDELETED_EVENT = new Event("ProposalDeleted", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
    ;

    public static final Event VOTECASTED_EVENT = new Event("VoteCasted", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}));
    ;

    @Deprecated
    protected VotingContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected VotingContract(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected VotingContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected VotingContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<OwnershipTransferredEventResponse> getOwnershipTransferredEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, transactionReceipt);
        ArrayList<OwnershipTransferredEventResponse> responses = new ArrayList<OwnershipTransferredEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, OwnershipTransferredEventResponse>() {
            @Override
            public OwnershipTransferredEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, log);
                OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
                typedResponse.log = log;
                typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(OWNERSHIPTRANSFERRED_EVENT));
        return ownershipTransferredEventFlowable(filter);
    }

    public static List<ProposalCreatedEventResponse> getProposalCreatedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(PROPOSALCREATED_EVENT, transactionReceipt);
        ArrayList<ProposalCreatedEventResponse> responses = new ArrayList<ProposalCreatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ProposalCreatedEventResponse typedResponse = new ProposalCreatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.proposalNumber = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.title = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ProposalCreatedEventResponse> proposalCreatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, ProposalCreatedEventResponse>() {
            @Override
            public ProposalCreatedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(PROPOSALCREATED_EVENT, log);
                ProposalCreatedEventResponse typedResponse = new ProposalCreatedEventResponse();
                typedResponse.log = log;
                typedResponse.proposalNumber = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.title = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ProposalCreatedEventResponse> proposalCreatedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(PROPOSALCREATED_EVENT));
        return proposalCreatedEventFlowable(filter);
    }

    public static List<ProposalDeletedEventResponse> getProposalDeletedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(PROPOSALDELETED_EVENT, transactionReceipt);
        ArrayList<ProposalDeletedEventResponse> responses = new ArrayList<ProposalDeletedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ProposalDeletedEventResponse typedResponse = new ProposalDeletedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.proposalNumber = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ProposalDeletedEventResponse> proposalDeletedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, ProposalDeletedEventResponse>() {
            @Override
            public ProposalDeletedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(PROPOSALDELETED_EVENT, log);
                ProposalDeletedEventResponse typedResponse = new ProposalDeletedEventResponse();
                typedResponse.log = log;
                typedResponse.proposalNumber = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ProposalDeletedEventResponse> proposalDeletedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(PROPOSALDELETED_EVENT));
        return proposalDeletedEventFlowable(filter);
    }

    public static List<VoteCastedEventResponse> getVoteCastedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(VOTECASTED_EVENT, transactionReceipt);
        ArrayList<VoteCastedEventResponse> responses = new ArrayList<VoteCastedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            VoteCastedEventResponse typedResponse = new VoteCastedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.proposalNumber = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.inFavor = (Boolean) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<VoteCastedEventResponse> voteCastedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, VoteCastedEventResponse>() {
            @Override
            public VoteCastedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(VOTECASTED_EVENT, log);
                VoteCastedEventResponse typedResponse = new VoteCastedEventResponse();
                typedResponse.log = log;
                typedResponse.proposalNumber = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.inFavor = (Boolean) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<VoteCastedEventResponse> voteCastedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(VOTECASTED_EVENT));
        return voteCastedEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> createProposal(String uuid, String title, String description, BigInteger durationInDays) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_CREATEPROPOSAL, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(uuid), 
                new org.web3j.abi.datatypes.Utf8String(title), 
                new org.web3j.abi.datatypes.Utf8String(description), 
                new org.web3j.abi.datatypes.generated.Uint256(durationInDays)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> deleteProposal(BigInteger proposalNumber) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_DELETEPROPOSAL, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(proposalNumber)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<ProposalRaw> getProposalDetails(BigInteger proposalNumber) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETPROPOSALDETAILS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(proposalNumber)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<ProposalRaw>() {}));
        return executeRemoteCallSingleValueReturn(function, ProposalRaw.class);
    }

    public RemoteFunctionCall<List> getProposalsList() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETPROPOSALSLIST, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<ProposalRaw>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<Boolean> hasVoted(BigInteger param0, String param1) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_HASVOTED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0), 
                new org.web3j.abi.datatypes.Address(160, param1)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<BigInteger> maxProposals() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_MAXPROPOSALS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> owner() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<Tuple8<BigInteger, String, String, String, BigInteger, BigInteger, BigInteger, BigInteger>> proposals(BigInteger param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_PROPOSALS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple8<BigInteger, String, String, String, BigInteger, BigInteger, BigInteger, BigInteger>>(function,
                new Callable<Tuple8<BigInteger, String, String, String, BigInteger, BigInteger, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple8<BigInteger, String, String, String, BigInteger, BigInteger, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple8<BigInteger, String, String, String, BigInteger, BigInteger, BigInteger, BigInteger>(
                                (BigInteger) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (String) results.get(2).getValue(), 
                                (String) results.get(3).getValue(), 
                                (BigInteger) results.get(4).getValue(), 
                                (BigInteger) results.get(5).getValue(), 
                                (BigInteger) results.get(6).getValue(), 
                                (BigInteger) results.get(7).getValue());
                    }
                });
    }

    public RemoteFunctionCall<TransactionReceipt> renounceOwnership() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_RENOUNCEOWNERSHIP, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> transferOwnership(String newOwner) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_TRANSFEROWNERSHIP, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, newOwner)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> vote(BigInteger proposalNumber, Boolean inFavor) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_VOTE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(proposalNumber), 
                new org.web3j.abi.datatypes.Bool(inFavor)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static VotingContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new VotingContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static VotingContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new VotingContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static VotingContract load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new VotingContract(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static VotingContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new VotingContract(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<VotingContract> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(VotingContract.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<VotingContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(VotingContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<VotingContract> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(VotingContract.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<VotingContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(VotingContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class ProposalRaw extends DynamicStruct {
        public BigInteger number;

        public String uuid;

        public String title;

        public String description;

        public BigInteger votesFor;

        public BigInteger votesAgainst;

        public BigInteger creationTime;

        public BigInteger expirationTime;

        public ProposalRaw(BigInteger number, String uuid, String title, String description, BigInteger votesFor, BigInteger votesAgainst, BigInteger creationTime, BigInteger expirationTime) {
            super(new org.web3j.abi.datatypes.generated.Uint256(number), 
                    new org.web3j.abi.datatypes.Utf8String(uuid), 
                    new org.web3j.abi.datatypes.Utf8String(title), 
                    new org.web3j.abi.datatypes.Utf8String(description), 
                    new org.web3j.abi.datatypes.generated.Uint256(votesFor), 
                    new org.web3j.abi.datatypes.generated.Uint256(votesAgainst), 
                    new org.web3j.abi.datatypes.generated.Uint256(creationTime), 
                    new org.web3j.abi.datatypes.generated.Uint256(expirationTime));
            this.number = number;
            this.uuid = uuid;
            this.title = title;
            this.description = description;
            this.votesFor = votesFor;
            this.votesAgainst = votesAgainst;
            this.creationTime = creationTime;
            this.expirationTime = expirationTime;
        }

        public ProposalRaw(Uint256 number, Utf8String uuid, Utf8String title, Utf8String description, Uint256 votesFor, Uint256 votesAgainst, Uint256 creationTime, Uint256 expirationTime) {
            super(number, uuid, title, description, votesFor, votesAgainst, creationTime, expirationTime);
            this.number = number.getValue();
            this.uuid = uuid.getValue();
            this.title = title.getValue();
            this.description = description.getValue();
            this.votesFor = votesFor.getValue();
            this.votesAgainst = votesAgainst.getValue();
            this.creationTime = creationTime.getValue();
            this.expirationTime = expirationTime.getValue();
        }
    }

    public static class OwnershipTransferredEventResponse extends BaseEventResponse {
        public String previousOwner;

        public String newOwner;
    }

    public static class ProposalCreatedEventResponse extends BaseEventResponse {
        public BigInteger proposalNumber;

        public String title;
    }

    public static class ProposalDeletedEventResponse extends BaseEventResponse {
        public BigInteger proposalNumber;
    }

    public static class VoteCastedEventResponse extends BaseEventResponse {
        public BigInteger proposalNumber;

        public Boolean inFavor;
    }
}
