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
import org.web3j.tuples.generated.Tuple7;
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
    public static final String BINARY = "6080604052600a600355348015610014575f80fd5b5061003161002661003660201b60201c565b61003d60201b60201c565b6100fe565b5f33905090565b5f805f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff169050815f806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508173ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e060405160405180910390a35050565b611fa18061010b5f395ff3fe608060405234801561000f575f80fd5b50600436106100a7575f3560e01c8063578178c01161006f578063578178c01461017b578063715018a6146101995780638259d553146101a35780638da5cb5b146101bf578063c9d27afe146101dd578063f2fde38b146101f9576100a7565b8063013cf08b146100ab5780632f3fe24c146100e15780633b4d01a7146100ff578063438596321461012f5780634c15676b1461015f575b5f80fd5b6100c560048036038101906100c0919061105b565b610215565b6040516100d8979695949392919061111f565b60405180910390f35b6100e96103f4565b6040516100f691906111a1565b60405180910390f35b6101196004803603810190610114919061105b565b6103fa565b60405161012691906112b8565b60405180910390f35b61014960048036038101906101449190611332565b61060b565b604051610156919061138a565b60405180910390f35b610179600480360381019061017491906114cf565b610635565b005b610183610858565b60405161019091906116e9565b60405180910390f35b6101a1610a8b565b005b6101bd60048036038101906101b8919061105b565b610a9e565b005b6101c7610b92565b6040516101d49190611718565b60405180910390f35b6101f760048036038101906101f2919061175b565b610bb9565b005b610213600480360381019061020e9190611799565b610dbe565b005b60018181548110610224575f80fd5b905f5260205f2090600702015f91509050805f015490806001018054610249906117f1565b80601f0160208091040260200160405190810160405280929190818152602001828054610275906117f1565b80156102c05780601f10610297576101008083540402835291602001916102c0565b820191905f5260205f20905b8154815290600101906020018083116102a357829003601f168201915b5050505050908060020180546102d5906117f1565b80601f0160208091040260200160405190810160405280929190818152602001828054610301906117f1565b801561034c5780601f106103235761010080835404028352916020019161034c565b820191905f5260205f20905b81548152906001019060200180831161032f57829003601f168201915b505050505090806003018054610361906117f1565b80601f016020809104026020016040519081016040528092919081815260200182805461038d906117f1565b80156103d85780601f106103af576101008083540402835291602001916103d8565b820191905f5260205f20905b8154815290600101906020018083116103bb57829003601f168201915b5050505050908060040154908060050154908060060154905087565b60035481565b610402610f86565b6001828154811061041657610415611821565b5b905f5260205f2090600702016040518060e00160405290815f8201548152602001600182018054610446906117f1565b80601f0160208091040260200160405190810160405280929190818152602001828054610472906117f1565b80156104bd5780601f10610494576101008083540402835291602001916104bd565b820191905f5260205f20905b8154815290600101906020018083116104a057829003601f168201915b505050505081526020016002820180546104d6906117f1565b80601f0160208091040260200160405190810160405280929190818152602001828054610502906117f1565b801561054d5780601f106105245761010080835404028352916020019161054d565b820191905f5260205f20905b81548152906001019060200180831161053057829003601f168201915b50505050508152602001600382018054610566906117f1565b80601f0160208091040260200160405190810160405280929190818152602001828054610592906117f1565b80156105dd5780601f106105b4576101008083540402835291602001916105dd565b820191905f5260205f20905b8154815290600101906020018083116105c057829003601f168201915b5050505050815260200160048201548152602001600582015481526020016006820154815250509050919050565b6002602052815f5260405f20602052805f5260405f205f915091509054906101000a900460ff1681565b61063d610e40565b60035460018054905010610686576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161067d90611898565b60405180910390fd5b60048460405161069691906118f0565b90815260200160405180910390205f9054906101000a900460ff16156106f1576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016106e890611976565b60405180910390fd5b5f6001805490509050610702610f86565b81815f0181815250508481604001819052508381606001819052508581602001819052505f620151808461073691906119c1565b426107419190611a02565b9050808260c0018181525050600182908060018154018082558091505060019003905f5260205f2090600702015f909190919091505f820151815f015560208201518160010190816107939190611bd2565b5060408201518160020190816107a99190611bd2565b5060608201518160030190816107bf9190611bd2565b506080820151816004015560a0820151816005015560c0820151816006015550507f9c770c289ab5bf7e57cb1d23c8ceae993aea46eb64847072fd3d78ca60d3e4328387604051610811929190611ca1565b60405180910390a1600160048860405161082b91906118f0565b90815260200160405180910390205f6101000a81548160ff02191690831515021790555050505050505050565b60606001805480602002602001604051908101604052809291908181526020015f905b82821015610a82578382905f5260205f2090600702016040518060e00160405290815f82015481526020016001820180546108b5906117f1565b80601f01602080910402602001604051908101604052809291908181526020018280546108e1906117f1565b801561092c5780601f106109035761010080835404028352916020019161092c565b820191905f5260205f20905b81548152906001019060200180831161090f57829003601f168201915b50505050508152602001600282018054610945906117f1565b80601f0160208091040260200160405190810160405280929190818152602001828054610971906117f1565b80156109bc5780601f10610993576101008083540402835291602001916109bc565b820191905f5260205f20905b81548152906001019060200180831161099f57829003601f168201915b505050505081526020016003820180546109d5906117f1565b80601f0160208091040260200160405190810160405280929190818152602001828054610a01906117f1565b8015610a4c5780601f10610a2357610100808354040283529160200191610a4c565b820191905f5260205f20905b815481529060010190602001808311610a2f57829003601f168201915b5050505050815260200160048201548152602001600582015481526020016006820154815250508152602001906001019061087b565b50505050905090565b610a93610e40565b610a9c5f610ebe565b565b610aa6610e40565b6001805490508110610aed576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610ae490611d19565b60405180910390fd5b60018181548110610b0157610b00611821565b5b905f5260205f2090600702015f8082015f9055600182015f610b239190610fbf565b600282015f610b329190610fbf565b600382015f610b419190610fbf565b600482015f9055600582015f9055600682015f905550507f61c0d93dc2b610877e420b107c8d12e9185e46e04a505da758cc7f7329ae545f81604051610b8791906111a1565b60405180910390a150565b5f805f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905090565b8160018181548110610bce57610bcd611821565b5b905f5260205f209060070201600601544210610c1f576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610c1690611d81565b60405180910390fd5b8260025f8281526020019081526020015f205f3373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f9054906101000a900460ff1615610cb9576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610cb090611de9565b60405180910390fd5b5f60018581548110610cce57610ccd611821565b5b905f5260205f20906007020190508315610d0057806004015f815480929190610cf690611e07565b9190505550610d1a565b806005015f815480929190610d1490611e07565b91905055505b600160025f8781526020019081526020015f205f3373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f6101000a81548160ff0219169083151502179055507f98b03f1463128a74cc9dd4acc43b54ef12ac07daacbcd621d2e4266091b7024a8585604051610daf929190611e4e565b60405180910390a15050505050565b610dc6610e40565b5f73ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff1603610e34576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610e2b90611ee5565b60405180910390fd5b610e3d81610ebe565b50565b610e48610f7f565b73ffffffffffffffffffffffffffffffffffffffff16610e66610b92565b73ffffffffffffffffffffffffffffffffffffffff1614610ebc576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610eb390611f4d565b60405180910390fd5b565b5f805f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff169050815f806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508173ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e060405160405180910390a35050565b5f33905090565b6040518060e001604052805f81526020016060815260200160608152602001606081526020015f81526020015f81526020015f81525090565b508054610fcb906117f1565b5f825580601f10610fdc5750610ff9565b601f0160209004905f5260205f2090810190610ff89190610ffc565b5b50565b5b80821115611013575f815f905550600101610ffd565b5090565b5f604051905090565b5f80fd5b5f80fd5b5f819050919050565b61103a81611028565b8114611044575f80fd5b50565b5f8135905061105581611031565b92915050565b5f602082840312156110705761106f611020565b5b5f61107d84828501611047565b91505092915050565b61108f81611028565b82525050565b5f81519050919050565b5f82825260208201905092915050565b5f5b838110156110cc5780820151818401526020810190506110b1565b5f8484015250505050565b5f601f19601f8301169050919050565b5f6110f182611095565b6110fb818561109f565b935061110b8185602086016110af565b611114816110d7565b840191505092915050565b5f60e0820190506111325f83018a611086565b818103602083015261114481896110e7565b9050818103604083015261115881886110e7565b9050818103606083015261116c81876110e7565b905061117b6080830186611086565b61118860a0830185611086565b61119560c0830184611086565b98975050505050505050565b5f6020820190506111b45f830184611086565b92915050565b6111c381611028565b82525050565b5f82825260208201905092915050565b5f6111e382611095565b6111ed81856111c9565b93506111fd8185602086016110af565b611206816110d7565b840191505092915050565b5f60e083015f8301516112265f8601826111ba565b506020830151848203602086015261123e82826111d9565b9150506040830151848203604086015261125882826111d9565b9150506060830151848203606086015261127282826111d9565b915050608083015161128760808601826111ba565b5060a083015161129a60a08601826111ba565b5060c08301516112ad60c08601826111ba565b508091505092915050565b5f6020820190508181035f8301526112d08184611211565b905092915050565b5f73ffffffffffffffffffffffffffffffffffffffff82169050919050565b5f611301826112d8565b9050919050565b611311816112f7565b811461131b575f80fd5b50565b5f8135905061132c81611308565b92915050565b5f806040838503121561134857611347611020565b5b5f61135585828601611047565b92505060206113668582860161131e565b9150509250929050565b5f8115159050919050565b61138481611370565b82525050565b5f60208201905061139d5f83018461137b565b92915050565b5f80fd5b5f80fd5b7f4e487b71000000000000000000000000000000000000000000000000000000005f52604160045260245ffd5b6113e1826110d7565b810181811067ffffffffffffffff82111715611400576113ff6113ab565b5b80604052505050565b5f611412611017565b905061141e82826113d8565b919050565b5f67ffffffffffffffff82111561143d5761143c6113ab565b5b611446826110d7565b9050602081019050919050565b828183375f83830152505050565b5f61147361146e84611423565b611409565b90508281526020810184848401111561148f5761148e6113a7565b5b61149a848285611453565b509392505050565b5f82601f8301126114b6576114b56113a3565b5b81356114c6848260208601611461565b91505092915050565b5f805f80608085870312156114e7576114e6611020565b5b5f85013567ffffffffffffffff81111561150457611503611024565b5b611510878288016114a2565b945050602085013567ffffffffffffffff81111561153157611530611024565b5b61153d878288016114a2565b935050604085013567ffffffffffffffff81111561155e5761155d611024565b5b61156a878288016114a2565b925050606061157b87828801611047565b91505092959194509250565b5f81519050919050565b5f82825260208201905092915050565b5f819050602082019050919050565b5f60e083015f8301516115c55f8601826111ba565b50602083015184820360208601526115dd82826111d9565b915050604083015184820360408601526115f782826111d9565b9150506060830151848203606086015261161182826111d9565b915050608083015161162660808601826111ba565b5060a083015161163960a08601826111ba565b5060c083015161164c60c08601826111ba565b508091505092915050565b5f61166283836115b0565b905092915050565b5f602082019050919050565b5f61168082611587565b61168a8185611591565b93508360208202850161169c856115a1565b805f5b858110156116d757848403895281516116b88582611657565b94506116c38361166a565b925060208a0199505060018101905061169f565b50829750879550505050505092915050565b5f6020820190508181035f8301526117018184611676565b905092915050565b611712816112f7565b82525050565b5f60208201905061172b5f830184611709565b92915050565b61173a81611370565b8114611744575f80fd5b50565b5f8135905061175581611731565b92915050565b5f806040838503121561177157611770611020565b5b5f61177e85828601611047565b925050602061178f85828601611747565b9150509250929050565b5f602082840312156117ae576117ad611020565b5b5f6117bb8482850161131e565b91505092915050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52602260045260245ffd5b5f600282049050600182168061180857607f821691505b60208210810361181b5761181a6117c4565b5b50919050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52603260045260245ffd5b7f4d61782070726f706f73616c73207265616368656400000000000000000000005f82015250565b5f61188260158361109f565b915061188d8261184e565b602082019050919050565b5f6020820190508181035f8301526118af81611876565b9050919050565b5f81905092915050565b5f6118ca82611095565b6118d481856118b6565b93506118e48185602086016110af565b80840191505092915050565b5f6118fb82846118c0565b915081905092915050565b7f50726f706f73616c20776974682074686973205555494420616c7265616479205f8201527f6578697374730000000000000000000000000000000000000000000000000000602082015250565b5f61196060268361109f565b915061196b82611906565b604082019050919050565b5f6020820190508181035f83015261198d81611954565b9050919050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52601160045260245ffd5b5f6119cb82611028565b91506119d683611028565b92508282026119e481611028565b915082820484148315176119fb576119fa611994565b5b5092915050565b5f611a0c82611028565b9150611a1783611028565b9250828201905080821115611a2f57611a2e611994565b5b92915050565b5f819050815f5260205f209050919050565b5f6020601f8301049050919050565b5f82821b905092915050565b5f60088302611a917fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff82611a56565b611a9b8683611a56565b95508019841693508086168417925050509392505050565b5f819050919050565b5f611ad6611ad1611acc84611028565b611ab3565b611028565b9050919050565b5f819050919050565b611aef83611abc565b611b03611afb82611add565b848454611a62565b825550505050565b5f90565b611b17611b0b565b611b22818484611ae6565b505050565b5b81811015611b4557611b3a5f82611b0f565b600181019050611b28565b5050565b601f821115611b8a57611b5b81611a35565b611b6484611a47565b81016020851015611b73578190505b611b87611b7f85611a47565b830182611b27565b50505b505050565b5f82821c905092915050565b5f611baa5f1984600802611b8f565b1980831691505092915050565b5f611bc28383611b9b565b9150826002028217905092915050565b611bdb82611095565b67ffffffffffffffff811115611bf457611bf36113ab565b5b611bfe82546117f1565b611c09828285611b49565b5f60209050601f831160018114611c3a575f8415611c28578287015190505b611c328582611bb7565b865550611c99565b601f198416611c4886611a35565b5f5b82811015611c6f57848901518255600182019150602085019450602081019050611c4a565b86831015611c8c5784890151611c88601f891682611b9b565b8355505b6001600288020188555050505b505050505050565b5f604082019050611cb45f830185611086565b8181036020830152611cc681846110e7565b90509392505050565b7f496e76616c69642070726f706f73616c206e756d6265720000000000000000005f82015250565b5f611d0360178361109f565b9150611d0e82611ccf565b602082019050919050565b5f6020820190508181035f830152611d3081611cf7565b9050919050565b7f566f74696e6720706572696f64206861732065787069726564000000000000005f82015250565b5f611d6b60198361109f565b9150611d7682611d37565b602082019050919050565b5f6020820190508181035f830152611d9881611d5f565b9050919050565b7f596f75206861766520616c726561647920766f746564000000000000000000005f82015250565b5f611dd360168361109f565b9150611dde82611d9f565b602082019050919050565b5f6020820190508181035f830152611e0081611dc7565b9050919050565b5f611e1182611028565b91507fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8203611e4357611e42611994565b5b600182019050919050565b5f604082019050611e615f830185611086565b611e6e602083018461137b565b9392505050565b7f4f776e61626c653a206e6577206f776e657220697320746865207a65726f20615f8201527f6464726573730000000000000000000000000000000000000000000000000000602082015250565b5f611ecf60268361109f565b9150611eda82611e75565b604082019050919050565b5f6020820190508181035f830152611efc81611ec3565b9050919050565b7f4f776e61626c653a2063616c6c6572206973206e6f7420746865206f776e65725f82015250565b5f611f3760208361109f565b9150611f4282611f03565b602082019050919050565b5f6020820190508181035f830152611f6481611f2b565b905091905056fea2646970667358221220893bbad1782b142642771260ce2ccb3c529dc88b0e58462a08140bd49e81f80d64736f6c63430008140033";

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

    public RemoteFunctionCall<Tuple7<BigInteger, String, String, String, BigInteger, BigInteger, BigInteger>> proposals(BigInteger param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_PROPOSALS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple7<BigInteger, String, String, String, BigInteger, BigInteger, BigInteger>>(function,
                new Callable<Tuple7<BigInteger, String, String, String, BigInteger, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple7<BigInteger, String, String, String, BigInteger, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple7<BigInteger, String, String, String, BigInteger, BigInteger, BigInteger>(
                                (BigInteger) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (String) results.get(2).getValue(), 
                                (String) results.get(3).getValue(), 
                                (BigInteger) results.get(4).getValue(), 
                                (BigInteger) results.get(5).getValue(), 
                                (BigInteger) results.get(6).getValue());
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

        public BigInteger expirationTime;

        public ProposalRaw(BigInteger number, String uuid, String title, String description, BigInteger votesFor, BigInteger votesAgainst, BigInteger expirationTime) {
            super(new org.web3j.abi.datatypes.generated.Uint256(number), 
                    new org.web3j.abi.datatypes.Utf8String(uuid), 
                    new org.web3j.abi.datatypes.Utf8String(title), 
                    new org.web3j.abi.datatypes.Utf8String(description), 
                    new org.web3j.abi.datatypes.generated.Uint256(votesFor), 
                    new org.web3j.abi.datatypes.generated.Uint256(votesAgainst), 
                    new org.web3j.abi.datatypes.generated.Uint256(expirationTime));
            this.number = number;
            this.uuid = uuid;
            this.title = title;
            this.description = description;
            this.votesFor = votesFor;
            this.votesAgainst = votesAgainst;
            this.expirationTime = expirationTime;
        }

        public ProposalRaw(Uint256 number, Utf8String uuid, Utf8String title, Utf8String description, Uint256 votesFor, Uint256 votesAgainst, Uint256 expirationTime) {
            super(number, uuid, title, description, votesFor, votesAgainst, expirationTime);
            this.number = number.getValue();
            this.uuid = uuid.getValue();
            this.title = title.getValue();
            this.description = description.getValue();
            this.votesFor = votesFor.getValue();
            this.votesAgainst = votesAgainst.getValue();
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
