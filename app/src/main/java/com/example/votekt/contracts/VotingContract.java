package com.example.votekt.contracts;

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
    public static final String BINARY = "6080604052600a600355348015610014575f80fd5b5061003161002661003660201b60201c565b61003d60201b60201c565b6100fe565b5f33905090565b5f805f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff169050815f806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508173ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e060405160405180910390a35050565b611f988061010b5f395ff3fe608060405234801561000f575f80fd5b50600436106100a7575f3560e01c8063578178c01161006f578063578178c01461017b578063715018a6146101995780638259d553146101a35780638da5cb5b146101bf578063c9d27afe146101dd578063f2fde38b146101f9576100a7565b8063013cf08b146100ab5780632f3fe24c146100e15780633b4d01a7146100ff578063438596321461012f5780634c15676b1461015f575b5f80fd5b6100c560048036038101906100c09190611052565b610215565b6040516100d89796959493929190611116565b60405180910390f35b6100e96103f4565b6040516100f69190611198565b60405180910390f35b61011960048036038101906101149190611052565b6103fa565b60405161012691906112af565b60405180910390f35b61014960048036038101906101449190611329565b61060b565b6040516101569190611381565b60405180910390f35b610179600480360381019061017491906114c6565b610635565b005b61018361084f565b60405161019091906116e0565b60405180910390f35b6101a1610a82565b005b6101bd60048036038101906101b89190611052565b610a95565b005b6101c7610b89565b6040516101d4919061170f565b60405180910390f35b6101f760048036038101906101f29190611752565b610bb0565b005b610213600480360381019061020e9190611790565b610db5565b005b60018181548110610224575f80fd5b905f5260205f2090600702015f91509050805f015490806001018054610249906117e8565b80601f0160208091040260200160405190810160405280929190818152602001828054610275906117e8565b80156102c05780601f10610297576101008083540402835291602001916102c0565b820191905f5260205f20905b8154815290600101906020018083116102a357829003601f168201915b5050505050908060020180546102d5906117e8565b80601f0160208091040260200160405190810160405280929190818152602001828054610301906117e8565b801561034c5780601f106103235761010080835404028352916020019161034c565b820191905f5260205f20905b81548152906001019060200180831161032f57829003601f168201915b505050505090806003018054610361906117e8565b80601f016020809104026020016040519081016040528092919081815260200182805461038d906117e8565b80156103d85780601f106103af576101008083540402835291602001916103d8565b820191905f5260205f20905b8154815290600101906020018083116103bb57829003601f168201915b5050505050908060040154908060050154908060060154905087565b60035481565b610402610f7d565b6001828154811061041657610415611818565b5b905f5260205f2090600702016040518060e00160405290815f8201548152602001600182018054610446906117e8565b80601f0160208091040260200160405190810160405280929190818152602001828054610472906117e8565b80156104bd5780601f10610494576101008083540402835291602001916104bd565b820191905f5260205f20905b8154815290600101906020018083116104a057829003601f168201915b505050505081526020016002820180546104d6906117e8565b80601f0160208091040260200160405190810160405280929190818152602001828054610502906117e8565b801561054d5780601f106105245761010080835404028352916020019161054d565b820191905f5260205f20905b81548152906001019060200180831161053057829003601f168201915b50505050508152602001600382018054610566906117e8565b80601f0160208091040260200160405190810160405280929190818152602001828054610592906117e8565b80156105dd5780601f106105b4576101008083540402835291602001916105dd565b820191905f5260205f20905b8154815290600101906020018083116105c057829003601f168201915b5050505050815260200160048201548152602001600582015481526020016006820154815250509050919050565b6002602052815f5260405f20602052805f5260405f205f915091509054906101000a900460ff1681565b61063d610e37565b60035460018054905010610686576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161067d9061188f565b60405180910390fd5b60048460405161069691906118e7565b90815260200160405180910390205f9054906101000a900460ff16156106f1576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016106e89061196d565b60405180910390fd5b5f6001805490509050610702610f7d565b81815f0181815250508481604001819052508381606001819052505f620151808461072d91906119b8565b4261073891906119f9565b9050808260c0018181525050600182908060018154018082558091505060019003905f5260205f2090600702015f909190919091505f820151815f0155602082015181600101908161078a9190611bc9565b5060408201518160020190816107a09190611bc9565b5060608201518160030190816107b69190611bc9565b506080820151816004015560a0820151816005015560c0820151816006015550507f9c770c289ab5bf7e57cb1d23c8ceae993aea46eb64847072fd3d78ca60d3e4328387604051610808929190611c98565b60405180910390a1600160048860405161082291906118e7565b90815260200160405180910390205f6101000a81548160ff02191690831515021790555050505050505050565b60606001805480602002602001604051908101604052809291908181526020015f905b82821015610a79578382905f5260205f2090600702016040518060e00160405290815f82015481526020016001820180546108ac906117e8565b80601f01602080910402602001604051908101604052809291908181526020018280546108d8906117e8565b80156109235780601f106108fa57610100808354040283529160200191610923565b820191905f5260205f20905b81548152906001019060200180831161090657829003601f168201915b5050505050815260200160028201805461093c906117e8565b80601f0160208091040260200160405190810160405280929190818152602001828054610968906117e8565b80156109b35780601f1061098a576101008083540402835291602001916109b3565b820191905f5260205f20905b81548152906001019060200180831161099657829003601f168201915b505050505081526020016003820180546109cc906117e8565b80601f01602080910402602001604051908101604052809291908181526020018280546109f8906117e8565b8015610a435780601f10610a1a57610100808354040283529160200191610a43565b820191905f5260205f20905b815481529060010190602001808311610a2657829003601f168201915b50505050508152602001600482015481526020016005820154815260200160068201548152505081526020019060010190610872565b50505050905090565b610a8a610e37565b610a935f610eb5565b565b610a9d610e37565b6001805490508110610ae4576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610adb90611d10565b60405180910390fd5b60018181548110610af857610af7611818565b5b905f5260205f2090600702015f8082015f9055600182015f610b1a9190610fb6565b600282015f610b299190610fb6565b600382015f610b389190610fb6565b600482015f9055600582015f9055600682015f905550507f61c0d93dc2b610877e420b107c8d12e9185e46e04a505da758cc7f7329ae545f81604051610b7e9190611198565b60405180910390a150565b5f805f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905090565b8160018181548110610bc557610bc4611818565b5b905f5260205f209060070201600601544210610c16576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610c0d90611d78565b60405180910390fd5b8260025f8281526020019081526020015f205f3373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f9054906101000a900460ff1615610cb0576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610ca790611de0565b60405180910390fd5b5f60018581548110610cc557610cc4611818565b5b905f5260205f20906007020190508315610cf757806004015f815480929190610ced90611dfe565b9190505550610d11565b806005015f815480929190610d0b90611dfe565b91905055505b600160025f8781526020019081526020015f205f3373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f6101000a81548160ff0219169083151502179055507f98b03f1463128a74cc9dd4acc43b54ef12ac07daacbcd621d2e4266091b7024a8585604051610da6929190611e45565b60405180910390a15050505050565b610dbd610e37565b5f73ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff1603610e2b576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610e2290611edc565b60405180910390fd5b610e3481610eb5565b50565b610e3f610f76565b73ffffffffffffffffffffffffffffffffffffffff16610e5d610b89565b73ffffffffffffffffffffffffffffffffffffffff1614610eb3576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610eaa90611f44565b60405180910390fd5b565b5f805f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff169050815f806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508173ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e060405160405180910390a35050565b5f33905090565b6040518060e001604052805f81526020016060815260200160608152602001606081526020015f81526020015f81526020015f81525090565b508054610fc2906117e8565b5f825580601f10610fd35750610ff0565b601f0160209004905f5260205f2090810190610fef9190610ff3565b5b50565b5b8082111561100a575f815f905550600101610ff4565b5090565b5f604051905090565b5f80fd5b5f80fd5b5f819050919050565b6110318161101f565b811461103b575f80fd5b50565b5f8135905061104c81611028565b92915050565b5f6020828403121561106757611066611017565b5b5f6110748482850161103e565b91505092915050565b6110868161101f565b82525050565b5f81519050919050565b5f82825260208201905092915050565b5f5b838110156110c35780820151818401526020810190506110a8565b5f8484015250505050565b5f601f19601f8301169050919050565b5f6110e88261108c565b6110f28185611096565b93506111028185602086016110a6565b61110b816110ce565b840191505092915050565b5f60e0820190506111295f83018a61107d565b818103602083015261113b81896110de565b9050818103604083015261114f81886110de565b9050818103606083015261116381876110de565b9050611172608083018661107d565b61117f60a083018561107d565b61118c60c083018461107d565b98975050505050505050565b5f6020820190506111ab5f83018461107d565b92915050565b6111ba8161101f565b82525050565b5f82825260208201905092915050565b5f6111da8261108c565b6111e481856111c0565b93506111f48185602086016110a6565b6111fd816110ce565b840191505092915050565b5f60e083015f83015161121d5f8601826111b1565b506020830151848203602086015261123582826111d0565b9150506040830151848203604086015261124f82826111d0565b9150506060830151848203606086015261126982826111d0565b915050608083015161127e60808601826111b1565b5060a083015161129160a08601826111b1565b5060c08301516112a460c08601826111b1565b508091505092915050565b5f6020820190508181035f8301526112c78184611208565b905092915050565b5f73ffffffffffffffffffffffffffffffffffffffff82169050919050565b5f6112f8826112cf565b9050919050565b611308816112ee565b8114611312575f80fd5b50565b5f81359050611323816112ff565b92915050565b5f806040838503121561133f5761133e611017565b5b5f61134c8582860161103e565b925050602061135d85828601611315565b9150509250929050565b5f8115159050919050565b61137b81611367565b82525050565b5f6020820190506113945f830184611372565b92915050565b5f80fd5b5f80fd5b7f4e487b71000000000000000000000000000000000000000000000000000000005f52604160045260245ffd5b6113d8826110ce565b810181811067ffffffffffffffff821117156113f7576113f66113a2565b5b80604052505050565b5f61140961100e565b905061141582826113cf565b919050565b5f67ffffffffffffffff821115611434576114336113a2565b5b61143d826110ce565b9050602081019050919050565b828183375f83830152505050565b5f61146a6114658461141a565b611400565b9050828152602081018484840111156114865761148561139e565b5b61149184828561144a565b509392505050565b5f82601f8301126114ad576114ac61139a565b5b81356114bd848260208601611458565b91505092915050565b5f805f80608085870312156114de576114dd611017565b5b5f85013567ffffffffffffffff8111156114fb576114fa61101b565b5b61150787828801611499565b945050602085013567ffffffffffffffff8111156115285761152761101b565b5b61153487828801611499565b935050604085013567ffffffffffffffff8111156115555761155461101b565b5b61156187828801611499565b92505060606115728782880161103e565b91505092959194509250565b5f81519050919050565b5f82825260208201905092915050565b5f819050602082019050919050565b5f60e083015f8301516115bc5f8601826111b1565b50602083015184820360208601526115d482826111d0565b915050604083015184820360408601526115ee82826111d0565b9150506060830151848203606086015261160882826111d0565b915050608083015161161d60808601826111b1565b5060a083015161163060a08601826111b1565b5060c083015161164360c08601826111b1565b508091505092915050565b5f61165983836115a7565b905092915050565b5f602082019050919050565b5f6116778261157e565b6116818185611588565b93508360208202850161169385611598565b805f5b858110156116ce57848403895281516116af858261164e565b94506116ba83611661565b925060208a01995050600181019050611696565b50829750879550505050505092915050565b5f6020820190508181035f8301526116f8818461166d565b905092915050565b611709816112ee565b82525050565b5f6020820190506117225f830184611700565b92915050565b61173181611367565b811461173b575f80fd5b50565b5f8135905061174c81611728565b92915050565b5f806040838503121561176857611767611017565b5b5f6117758582860161103e565b92505060206117868582860161173e565b9150509250929050565b5f602082840312156117a5576117a4611017565b5b5f6117b284828501611315565b91505092915050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52602260045260245ffd5b5f60028204905060018216806117ff57607f821691505b602082108103611812576118116117bb565b5b50919050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52603260045260245ffd5b7f4d61782070726f706f73616c73207265616368656400000000000000000000005f82015250565b5f611879601583611096565b915061188482611845565b602082019050919050565b5f6020820190508181035f8301526118a68161186d565b9050919050565b5f81905092915050565b5f6118c18261108c565b6118cb81856118ad565b93506118db8185602086016110a6565b80840191505092915050565b5f6118f282846118b7565b915081905092915050565b7f50726f706f73616c20776974682074686973205555494420616c7265616479205f8201527f6578697374730000000000000000000000000000000000000000000000000000602082015250565b5f611957602683611096565b9150611962826118fd565b604082019050919050565b5f6020820190508181035f8301526119848161194b565b9050919050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52601160045260245ffd5b5f6119c28261101f565b91506119cd8361101f565b92508282026119db8161101f565b915082820484148315176119f2576119f161198b565b5b5092915050565b5f611a038261101f565b9150611a0e8361101f565b9250828201905080821115611a2657611a2561198b565b5b92915050565b5f819050815f5260205f209050919050565b5f6020601f8301049050919050565b5f82821b905092915050565b5f60088302611a887fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff82611a4d565b611a928683611a4d565b95508019841693508086168417925050509392505050565b5f819050919050565b5f611acd611ac8611ac38461101f565b611aaa565b61101f565b9050919050565b5f819050919050565b611ae683611ab3565b611afa611af282611ad4565b848454611a59565b825550505050565b5f90565b611b0e611b02565b611b19818484611add565b505050565b5b81811015611b3c57611b315f82611b06565b600181019050611b1f565b5050565b601f821115611b8157611b5281611a2c565b611b5b84611a3e565b81016020851015611b6a578190505b611b7e611b7685611a3e565b830182611b1e565b50505b505050565b5f82821c905092915050565b5f611ba15f1984600802611b86565b1980831691505092915050565b5f611bb98383611b92565b9150826002028217905092915050565b611bd28261108c565b67ffffffffffffffff811115611beb57611bea6113a2565b5b611bf582546117e8565b611c00828285611b40565b5f60209050601f831160018114611c31575f8415611c1f578287015190505b611c298582611bae565b865550611c90565b601f198416611c3f86611a2c565b5f5b82811015611c6657848901518255600182019150602085019450602081019050611c41565b86831015611c835784890151611c7f601f891682611b92565b8355505b6001600288020188555050505b505050505050565b5f604082019050611cab5f83018561107d565b8181036020830152611cbd81846110de565b90509392505050565b7f496e76616c69642070726f706f73616c206e756d6265720000000000000000005f82015250565b5f611cfa601783611096565b9150611d0582611cc6565b602082019050919050565b5f6020820190508181035f830152611d2781611cee565b9050919050565b7f566f74696e6720706572696f64206861732065787069726564000000000000005f82015250565b5f611d62601983611096565b9150611d6d82611d2e565b602082019050919050565b5f6020820190508181035f830152611d8f81611d56565b9050919050565b7f596f75206861766520616c726561647920766f746564000000000000000000005f82015250565b5f611dca601683611096565b9150611dd582611d96565b602082019050919050565b5f6020820190508181035f830152611df781611dbe565b9050919050565b5f611e088261101f565b91507fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8203611e3a57611e3961198b565b5b600182019050919050565b5f604082019050611e585f83018561107d565b611e656020830184611372565b9392505050565b7f4f776e61626c653a206e6577206f776e657220697320746865207a65726f20615f8201527f6464726573730000000000000000000000000000000000000000000000000000602082015250565b5f611ec6602683611096565b9150611ed182611e6c565b604082019050919050565b5f6020820190508181035f830152611ef381611eba565b9050919050565b7f4f776e61626c653a2063616c6c6572206973206e6f7420746865206f776e65725f82015250565b5f611f2e602083611096565b9150611f3982611efa565b602082019050919050565b5f6020820190508181035f830152611f5b81611f22565b905091905056fea264697066735822122055cc51958febb259d5fe8457c51d9a9419a61e3aa6c035379dedf209f77a316064736f6c63430008140033";

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
