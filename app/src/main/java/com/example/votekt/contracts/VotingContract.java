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
import org.web3j.tuples.generated.Tuple5;
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
    public static final String BINARY = "60806040526005600355348015610014575f80fd5b5061003161002661003660201b60201c565b61003d60201b60201c565b6100fe565b5f33905090565b5f805f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff169050815f806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508173ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e060405160405180910390a35050565b611af28061010b5f395ff3fe608060405234801561000f575f80fd5b50600436106100a7575f3560e01c806365e481e21161006f57806365e481e21461017b578063715018a6146101975780638259d553146101a15780638da5cb5b146101bd578063c9d27afe146101db578063f2fde38b146101f7576100a7565b8063013cf08b146100ab5780632f3fe24c146100df5780633b4d01a7146100fd578063438596321461012d578063578178c01461015d575b5f80fd5b6100c560048036038101906100c09190610d8a565b610213565b6040516100d6959493929190610e4e565b60405180910390f35b6100e7610360565b6040516100f49190610ead565b60405180910390f35b61011760048036038101906101129190610d8a565b610366565b6040516101249190610f97565b60405180910390f35b61014760048036038101906101429190611011565b6104dd565b6040516101549190611069565b60405180910390f35b610165610507565b60405161017291906111b7565b60405180910390f35b61019560048036038101906101909190611303565b6106a0565b005b61019f6107dd565b005b6101bb60048036038101906101b69190610d8a565b6107f0565b005b6101c56108ce565b6040516101d29190611388565b60405180910390f35b6101f560048036038101906101f091906113cb565b6108f5565b005b610211600480360381019061020c9190611409565b610afa565b005b60018181548110610222575f80fd5b905f5260205f2090600502015f91509050805f01805461024190611461565b80601f016020809104026020016040519081016040528092919081815260200182805461026d90611461565b80156102b85780601f1061028f576101008083540402835291602001916102b8565b820191905f5260205f20905b81548152906001019060200180831161029b57829003601f168201915b5050505050908060010180546102cd90611461565b80601f01602080910402602001604051908101604052809291908181526020018280546102f990611461565b80156103445780601f1061031b57610100808354040283529160200191610344565b820191905f5260205f20905b81548152906001019060200180831161032757829003601f168201915b5050505050908060020154908060030154908060040154905085565b60035481565b61036e610cc2565b6001828154811061038257610381611491565b5b905f5260205f2090600502016040518060a00160405290815f820180546103a890611461565b80601f01602080910402602001604051908101604052809291908181526020018280546103d490611461565b801561041f5780601f106103f65761010080835404028352916020019161041f565b820191905f5260205f20905b81548152906001019060200180831161040257829003601f168201915b5050505050815260200160018201805461043890611461565b80601f016020809104026020016040519081016040528092919081815260200182805461046490611461565b80156104af5780601f10610486576101008083540402835291602001916104af565b820191905f5260205f20905b81548152906001019060200180831161049257829003601f168201915b5050505050815260200160028201548152602001600382015481526020016004820154815250509050919050565b6002602052815f5260405f20602052805f5260405f205f915091509054906101000a900460ff1681565b60606001805480602002602001604051908101604052809291908181526020015f905b82821015610697578382905f5260205f2090600502016040518060a00160405290815f8201805461055a90611461565b80601f016020809104026020016040519081016040528092919081815260200182805461058690611461565b80156105d15780601f106105a8576101008083540402835291602001916105d1565b820191905f5260205f20905b8154815290600101906020018083116105b457829003601f168201915b505050505081526020016001820180546105ea90611461565b80601f016020809104026020016040519081016040528092919081815260200182805461061690611461565b80156106615780601f1061063857610100808354040283529160200191610661565b820191905f5260205f20905b81548152906001019060200180831161064457829003601f168201915b5050505050815260200160028201548152602001600382015481526020016004820154815250508152602001906001019061052a565b50505050905090565b6106a8610b7c565b600354600180549050106106f1576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016106e890611508565b60405180910390fd5b5f6001805490509050610702610cc2565b83815f018190525082816020018190525062015180426107229190611553565b816080018181525050600181908060018154018082558091505060019003905f5260205f2090600502015f909190919091505f820151815f0190816107679190611723565b50602082015181600101908161077d9190611723565b5060408201518160020155606082015181600301556080820151816004015550507f9c770c289ab5bf7e57cb1d23c8ceae993aea46eb64847072fd3d78ca60d3e43282856040516107cf9291906117f2565b60405180910390a150505050565b6107e5610b7c565b6107ee5f610bfa565b565b6107f8610b7c565b600180549050811061083f576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016108369061186a565b60405180910390fd5b6001818154811061085357610852611491565b5b905f5260205f2090600502015f8082015f61086e9190610cee565b600182015f61087d9190610cee565b600282015f9055600382015f9055600482015f905550507f61c0d93dc2b610877e420b107c8d12e9185e46e04a505da758cc7f7329ae545f816040516108c39190610ead565b60405180910390a150565b5f805f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905090565b816001818154811061090a57610909611491565b5b905f5260205f20906005020160040154421061095b576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610952906118d2565b60405180910390fd5b8260025f8281526020019081526020015f205f3373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f9054906101000a900460ff16156109f5576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016109ec9061193a565b60405180910390fd5b5f60018581548110610a0a57610a09611491565b5b905f5260205f20906005020190508315610a3c57806002015f815480929190610a3290611958565b9190505550610a56565b806003015f815480929190610a5090611958565b91905055505b600160025f8781526020019081526020015f205f3373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f6101000a81548160ff0219169083151502179055507f98b03f1463128a74cc9dd4acc43b54ef12ac07daacbcd621d2e4266091b7024a8585604051610aeb92919061199f565b60405180910390a15050505050565b610b02610b7c565b5f73ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff1603610b70576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610b6790611a36565b60405180910390fd5b610b7981610bfa565b50565b610b84610cbb565b73ffffffffffffffffffffffffffffffffffffffff16610ba26108ce565b73ffffffffffffffffffffffffffffffffffffffff1614610bf8576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610bef90611a9e565b60405180910390fd5b565b5f805f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff169050815f806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508173ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e060405160405180910390a35050565b5f33905090565b6040518060a0016040528060608152602001606081526020015f81526020015f81526020015f81525090565b508054610cfa90611461565b5f825580601f10610d0b5750610d28565b601f0160209004905f5260205f2090810190610d279190610d2b565b5b50565b5b80821115610d42575f815f905550600101610d2c565b5090565b5f604051905090565b5f80fd5b5f80fd5b5f819050919050565b610d6981610d57565b8114610d73575f80fd5b50565b5f81359050610d8481610d60565b92915050565b5f60208284031215610d9f57610d9e610d4f565b5b5f610dac84828501610d76565b91505092915050565b5f81519050919050565b5f82825260208201905092915050565b5f5b83811015610dec578082015181840152602081019050610dd1565b5f8484015250505050565b5f601f19601f8301169050919050565b5f610e1182610db5565b610e1b8185610dbf565b9350610e2b818560208601610dcf565b610e3481610df7565b840191505092915050565b610e4881610d57565b82525050565b5f60a0820190508181035f830152610e668188610e07565b90508181036020830152610e7a8187610e07565b9050610e896040830186610e3f565b610e966060830185610e3f565b610ea36080830184610e3f565b9695505050505050565b5f602082019050610ec05f830184610e3f565b92915050565b5f82825260208201905092915050565b5f610ee082610db5565b610eea8185610ec6565b9350610efa818560208601610dcf565b610f0381610df7565b840191505092915050565b610f1781610d57565b82525050565b5f60a083015f8301518482035f860152610f378282610ed6565b91505060208301518482036020860152610f518282610ed6565b9150506040830151610f666040860182610f0e565b506060830151610f796060860182610f0e565b506080830151610f8c6080860182610f0e565b508091505092915050565b5f6020820190508181035f830152610faf8184610f1d565b905092915050565b5f73ffffffffffffffffffffffffffffffffffffffff82169050919050565b5f610fe082610fb7565b9050919050565b610ff081610fd6565b8114610ffa575f80fd5b50565b5f8135905061100b81610fe7565b92915050565b5f806040838503121561102757611026610d4f565b5b5f61103485828601610d76565b925050602061104585828601610ffd565b9150509250929050565b5f8115159050919050565b6110638161104f565b82525050565b5f60208201905061107c5f83018461105a565b92915050565b5f81519050919050565b5f82825260208201905092915050565b5f819050602082019050919050565b5f60a083015f8301518482035f8601526110c58282610ed6565b915050602083015184820360208601526110df8282610ed6565b91505060408301516110f46040860182610f0e565b5060608301516111076060860182610f0e565b50608083015161111a6080860182610f0e565b508091505092915050565b5f61113083836110ab565b905092915050565b5f602082019050919050565b5f61114e82611082565b611158818561108c565b93508360208202850161116a8561109c565b805f5b858110156111a557848403895281516111868582611125565b945061119183611138565b925060208a0199505060018101905061116d565b50829750879550505050505092915050565b5f6020820190508181035f8301526111cf8184611144565b905092915050565b5f80fd5b5f80fd5b7f4e487b71000000000000000000000000000000000000000000000000000000005f52604160045260245ffd5b61121582610df7565b810181811067ffffffffffffffff82111715611234576112336111df565b5b80604052505050565b5f611246610d46565b9050611252828261120c565b919050565b5f67ffffffffffffffff821115611271576112706111df565b5b61127a82610df7565b9050602081019050919050565b828183375f83830152505050565b5f6112a76112a284611257565b61123d565b9050828152602081018484840111156112c3576112c26111db565b5b6112ce848285611287565b509392505050565b5f82601f8301126112ea576112e96111d7565b5b81356112fa848260208601611295565b91505092915050565b5f806040838503121561131957611318610d4f565b5b5f83013567ffffffffffffffff81111561133657611335610d53565b5b611342858286016112d6565b925050602083013567ffffffffffffffff81111561136357611362610d53565b5b61136f858286016112d6565b9150509250929050565b61138281610fd6565b82525050565b5f60208201905061139b5f830184611379565b92915050565b6113aa8161104f565b81146113b4575f80fd5b50565b5f813590506113c5816113a1565b92915050565b5f80604083850312156113e1576113e0610d4f565b5b5f6113ee85828601610d76565b92505060206113ff858286016113b7565b9150509250929050565b5f6020828403121561141e5761141d610d4f565b5b5f61142b84828501610ffd565b91505092915050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52602260045260245ffd5b5f600282049050600182168061147857607f821691505b60208210810361148b5761148a611434565b5b50919050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52603260045260245ffd5b7f4d61782070726f706f73616c73207265616368656400000000000000000000005f82015250565b5f6114f2601583610dbf565b91506114fd826114be565b602082019050919050565b5f6020820190508181035f83015261151f816114e6565b9050919050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52601160045260245ffd5b5f61155d82610d57565b915061156883610d57565b92508282019050808211156115805761157f611526565b5b92915050565b5f819050815f5260205f209050919050565b5f6020601f8301049050919050565b5f82821b905092915050565b5f600883026115e27fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff826115a7565b6115ec86836115a7565b95508019841693508086168417925050509392505050565b5f819050919050565b5f61162761162261161d84610d57565b611604565b610d57565b9050919050565b5f819050919050565b6116408361160d565b61165461164c8261162e565b8484546115b3565b825550505050565b5f90565b61166861165c565b611673818484611637565b505050565b5b818110156116965761168b5f82611660565b600181019050611679565b5050565b601f8211156116db576116ac81611586565b6116b584611598565b810160208510156116c4578190505b6116d86116d085611598565b830182611678565b50505b505050565b5f82821c905092915050565b5f6116fb5f19846008026116e0565b1980831691505092915050565b5f61171383836116ec565b9150826002028217905092915050565b61172c82610db5565b67ffffffffffffffff811115611745576117446111df565b5b61174f8254611461565b61175a82828561169a565b5f60209050601f83116001811461178b575f8415611779578287015190505b6117838582611708565b8655506117ea565b601f19841661179986611586565b5f5b828110156117c05784890151825560018201915060208501945060208101905061179b565b868310156117dd57848901516117d9601f8916826116ec565b8355505b6001600288020188555050505b505050505050565b5f6040820190506118055f830185610e3f565b81810360208301526118178184610e07565b90509392505050565b7f496e76616c69642070726f706f73616c204944000000000000000000000000005f82015250565b5f611854601383610dbf565b915061185f82611820565b602082019050919050565b5f6020820190508181035f83015261188181611848565b9050919050565b7f566f74696e6720706572696f64206861732065787069726564000000000000005f82015250565b5f6118bc601983610dbf565b91506118c782611888565b602082019050919050565b5f6020820190508181035f8301526118e9816118b0565b9050919050565b7f596f75206861766520616c726561647920766f746564000000000000000000005f82015250565b5f611924601683610dbf565b915061192f826118f0565b602082019050919050565b5f6020820190508181035f83015261195181611918565b9050919050565b5f61196282610d57565b91507fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff820361199457611993611526565b5b600182019050919050565b5f6040820190506119b25f830185610e3f565b6119bf602083018461105a565b9392505050565b7f4f776e61626c653a206e6577206f776e657220697320746865207a65726f20615f8201527f6464726573730000000000000000000000000000000000000000000000000000602082015250565b5f611a20602683610dbf565b9150611a2b826119c6565b604082019050919050565b5f6020820190508181035f830152611a4d81611a14565b9050919050565b7f4f776e61626c653a2063616c6c6572206973206e6f7420746865206f776e65725f82015250565b5f611a88602083610dbf565b9150611a9382611a54565b602082019050919050565b5f6020820190508181035f830152611ab581611a7c565b905091905056fea2646970667358221220f27fc8e2a66456665d83359b7a1b6809f076d790e01f1808a29c1e603f45872064736f6c63430008140033";

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
            typedResponse.proposalId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
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
                typedResponse.proposalId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
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
            typedResponse.proposalId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
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
                typedResponse.proposalId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
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
            typedResponse.proposalId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
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
                typedResponse.proposalId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
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

    public RemoteFunctionCall<TransactionReceipt> createProposal(String title, String description) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_CREATEPROPOSAL,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(title),
                        new org.web3j.abi.datatypes.Utf8String(description)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> deleteProposal(BigInteger proposalId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_DELETEPROPOSAL,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(proposalId)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<ProposalRaw> getProposalDetails(BigInteger proposalId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETPROPOSALDETAILS,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(proposalId)),
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

    public RemoteFunctionCall<Tuple5<String, String, BigInteger, BigInteger, BigInteger>> proposals(BigInteger param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_PROPOSALS,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple5<String, String, BigInteger, BigInteger, BigInteger>>(function,
                new Callable<Tuple5<String, String, BigInteger, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple5<String, String, BigInteger, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple5<String, String, BigInteger, BigInteger, BigInteger>(
                                (String) results.get(0).getValue(),
                                (String) results.get(1).getValue(),
                                (BigInteger) results.get(2).getValue(),
                                (BigInteger) results.get(3).getValue(),
                                (BigInteger) results.get(4).getValue());
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

    public RemoteFunctionCall<TransactionReceipt> vote(BigInteger proposalId, Boolean inFavor) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_VOTE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(proposalId),
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
        public String title;

        public String description;

        public BigInteger votesFor;

        public BigInteger votesAgainst;

        public BigInteger expirationTime;

        public ProposalRaw(String title, String description, BigInteger votesFor, BigInteger votesAgainst, BigInteger expirationTime) {
            super(new org.web3j.abi.datatypes.Utf8String(title),
                    new org.web3j.abi.datatypes.Utf8String(description),
                    new org.web3j.abi.datatypes.generated.Uint256(votesFor),
                    new org.web3j.abi.datatypes.generated.Uint256(votesAgainst),
                    new org.web3j.abi.datatypes.generated.Uint256(expirationTime));
            this.title = title;
            this.description = description;
            this.votesFor = votesFor;
            this.votesAgainst = votesAgainst;
            this.expirationTime = expirationTime;
        }

        public ProposalRaw(Utf8String title, Utf8String description, Uint256 votesFor, Uint256 votesAgainst, Uint256 expirationTime) {
            super(title, description, votesFor, votesAgainst, expirationTime);
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
        public BigInteger proposalId;

        public String title;
    }

    public static class ProposalDeletedEventResponse extends BaseEventResponse {
        public BigInteger proposalId;
    }

    public static class VoteCastedEventResponse extends BaseEventResponse {
        public BigInteger proposalId;

        public Boolean inFavor;
    }
}
