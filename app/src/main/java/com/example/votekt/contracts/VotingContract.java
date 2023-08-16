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
import org.web3j.tuples.generated.Tuple6;
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
    public static final String BINARY = "60806040526005600355348015610014575f80fd5b5061003161002661003660201b60201c565b61003d60201b60201c565b6100fe565b5f33905090565b5f805f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff169050815f806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508173ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e060405160405180910390a35050565b611bc68061010b5f395ff3fe608060405234801561000f575f80fd5b50600436106100a7575f3560e01c8063578178c01161006f578063578178c01461017a578063715018a6146101985780638259d553146101a25780638da5cb5b146101be578063c9d27afe146101dc578063f2fde38b146101f8576100a7565b8063013cf08b146100ab5780630ce0ebf4146100e05780632f3fe24c146100fc5780633b4d01a71461011a578063438596321461014a575b5f80fd5b6100c560048036038101906100c09190610dd7565b610214565b6040516100d796959493929190610e9b565b60405180910390f35b6100fa60048036038101906100f59190611034565b610367565b005b6101046104c9565b60405161011191906110bc565b60405180910390f35b610134600480360381019061012f9190610dd7565b6104cf565b60405161014191906111b9565b60405180910390f35b610164600480360381019061015f9190611233565b610650565b604051610171919061128b565b60405180910390f35b61018261067a565b60405161018f91906113ec565b60405180910390f35b6101a061081d565b005b6101bc60048036038101906101b79190610dd7565b610830565b005b6101c6610915565b6040516101d3919061141b565b60405180910390f35b6101f660048036038101906101f1919061145e565b61093c565b005b610212600480360381019061020d919061149c565b610b41565b005b60018181548110610223575f80fd5b905f5260205f2090600602015f91509050805f015490806001018054610248906114f4565b80601f0160208091040260200160405190810160405280929190818152602001828054610274906114f4565b80156102bf5780601f10610296576101008083540402835291602001916102bf565b820191905f5260205f20905b8154815290600101906020018083116102a257829003601f168201915b5050505050908060020180546102d4906114f4565b80601f0160208091040260200160405190810160405280929190818152602001828054610300906114f4565b801561034b5780601f106103225761010080835404028352916020019161034b565b820191905f5260205f20905b81548152906001019060200180831161032e57829003601f168201915b5050505050908060030154908060040154908060050154905086565b61036f610bc3565b600354600180549050106103b8576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016103af9061156e565b60405180910390fd5b5f60018054905090506103c9610d09565b81815f0181815250508481602001819052508381604001819052505f62015180846103f491906115b9565b426103ff91906115fa565b9050808260a0018181525050600182908060018154018082558091505060019003905f5260205f2090600602015f909190919091505f820151815f0155602082015181600101908161045191906117ca565b50604082015181600201908161046791906117ca565b50606082015181600301556080820151816004015560a0820151816005015550507f9c770c289ab5bf7e57cb1d23c8ceae993aea46eb64847072fd3d78ca60d3e43283876040516104b9929190611899565b60405180910390a1505050505050565b60035481565b6104d7610d09565b600182815481106104eb576104ea6118c7565b5b905f5260205f2090600602016040518060c00160405290815f820154815260200160018201805461051b906114f4565b80601f0160208091040260200160405190810160405280929190818152602001828054610547906114f4565b80156105925780601f1061056957610100808354040283529160200191610592565b820191905f5260205f20905b81548152906001019060200180831161057557829003601f168201915b505050505081526020016002820180546105ab906114f4565b80601f01602080910402602001604051908101604052809291908181526020018280546105d7906114f4565b80156106225780601f106105f957610100808354040283529160200191610622565b820191905f5260205f20905b81548152906001019060200180831161060557829003601f168201915b5050505050815260200160038201548152602001600482015481526020016005820154815250509050919050565b6002602052815f5260405f20602052805f5260405f205f915091509054906101000a900460ff1681565b60606001805480602002602001604051908101604052809291908181526020015f905b82821015610814578382905f5260205f2090600602016040518060c00160405290815f82015481526020016001820180546106d7906114f4565b80601f0160208091040260200160405190810160405280929190818152602001828054610703906114f4565b801561074e5780601f106107255761010080835404028352916020019161074e565b820191905f5260205f20905b81548152906001019060200180831161073157829003601f168201915b50505050508152602001600282018054610767906114f4565b80601f0160208091040260200160405190810160405280929190818152602001828054610793906114f4565b80156107de5780601f106107b5576101008083540402835291602001916107de565b820191905f5260205f20905b8154815290600101906020018083116107c157829003601f168201915b5050505050815260200160038201548152602001600482015481526020016005820154815250508152602001906001019061069d565b50505050905090565b610825610bc3565b61082e5f610c41565b565b610838610bc3565b600180549050811061087f576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016108769061193e565b60405180910390fd5b60018181548110610893576108926118c7565b5b905f5260205f2090600602015f8082015f9055600182015f6108b59190610d3b565b600282015f6108c49190610d3b565b600382015f9055600482015f9055600582015f905550507f61c0d93dc2b610877e420b107c8d12e9185e46e04a505da758cc7f7329ae545f8160405161090a91906110bc565b60405180910390a150565b5f805f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905090565b8160018181548110610951576109506118c7565b5b905f5260205f2090600602016005015442106109a2576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610999906119a6565b60405180910390fd5b8260025f8281526020019081526020015f205f3373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f9054906101000a900460ff1615610a3c576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610a3390611a0e565b60405180910390fd5b5f60018581548110610a5157610a506118c7565b5b905f5260205f20906006020190508315610a8357806003015f815480929190610a7990611a2c565b9190505550610a9d565b806004015f815480929190610a9790611a2c565b91905055505b600160025f8781526020019081526020015f205f3373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f6101000a81548160ff0219169083151502179055507f98b03f1463128a74cc9dd4acc43b54ef12ac07daacbcd621d2e4266091b7024a8585604051610b32929190611a73565b60405180910390a15050505050565b610b49610bc3565b5f73ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff1603610bb7576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610bae90611b0a565b60405180910390fd5b610bc081610c41565b50565b610bcb610d02565b73ffffffffffffffffffffffffffffffffffffffff16610be9610915565b73ffffffffffffffffffffffffffffffffffffffff1614610c3f576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610c3690611b72565b60405180910390fd5b565b5f805f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff169050815f806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508173ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e060405160405180910390a35050565b5f33905090565b6040518060c001604052805f815260200160608152602001606081526020015f81526020015f81526020015f81525090565b508054610d47906114f4565b5f825580601f10610d585750610d75565b601f0160209004905f5260205f2090810190610d749190610d78565b5b50565b5b80821115610d8f575f815f905550600101610d79565b5090565b5f604051905090565b5f80fd5b5f80fd5b5f819050919050565b610db681610da4565b8114610dc0575f80fd5b50565b5f81359050610dd181610dad565b92915050565b5f60208284031215610dec57610deb610d9c565b5b5f610df984828501610dc3565b91505092915050565b610e0b81610da4565b82525050565b5f81519050919050565b5f82825260208201905092915050565b5f5b83811015610e48578082015181840152602081019050610e2d565b5f8484015250505050565b5f601f19601f8301169050919050565b5f610e6d82610e11565b610e778185610e1b565b9350610e87818560208601610e2b565b610e9081610e53565b840191505092915050565b5f60c082019050610eae5f830189610e02565b8181036020830152610ec08188610e63565b90508181036040830152610ed48187610e63565b9050610ee36060830186610e02565b610ef06080830185610e02565b610efd60a0830184610e02565b979650505050505050565b5f80fd5b5f80fd5b7f4e487b71000000000000000000000000000000000000000000000000000000005f52604160045260245ffd5b610f4682610e53565b810181811067ffffffffffffffff82111715610f6557610f64610f10565b5b80604052505050565b5f610f77610d93565b9050610f838282610f3d565b919050565b5f67ffffffffffffffff821115610fa257610fa1610f10565b5b610fab82610e53565b9050602081019050919050565b828183375f83830152505050565b5f610fd8610fd384610f88565b610f6e565b905082815260208101848484011115610ff457610ff3610f0c565b5b610fff848285610fb8565b509392505050565b5f82601f83011261101b5761101a610f08565b5b813561102b848260208601610fc6565b91505092915050565b5f805f6060848603121561104b5761104a610d9c565b5b5f84013567ffffffffffffffff81111561106857611067610da0565b5b61107486828701611007565b935050602084013567ffffffffffffffff81111561109557611094610da0565b5b6110a186828701611007565b92505060406110b286828701610dc3565b9150509250925092565b5f6020820190506110cf5f830184610e02565b92915050565b6110de81610da4565b82525050565b5f82825260208201905092915050565b5f6110fe82610e11565b61110881856110e4565b9350611118818560208601610e2b565b61112181610e53565b840191505092915050565b5f60c083015f8301516111415f8601826110d5565b506020830151848203602086015261115982826110f4565b9150506040830151848203604086015261117382826110f4565b915050606083015161118860608601826110d5565b50608083015161119b60808601826110d5565b5060a08301516111ae60a08601826110d5565b508091505092915050565b5f6020820190508181035f8301526111d1818461112c565b905092915050565b5f73ffffffffffffffffffffffffffffffffffffffff82169050919050565b5f611202826111d9565b9050919050565b611212816111f8565b811461121c575f80fd5b50565b5f8135905061122d81611209565b92915050565b5f806040838503121561124957611248610d9c565b5b5f61125685828601610dc3565b92505060206112678582860161121f565b9150509250929050565b5f8115159050919050565b61128581611271565b82525050565b5f60208201905061129e5f83018461127c565b92915050565b5f81519050919050565b5f82825260208201905092915050565b5f819050602082019050919050565b5f60c083015f8301516112e25f8601826110d5565b50602083015184820360208601526112fa82826110f4565b9150506040830151848203604086015261131482826110f4565b915050606083015161132960608601826110d5565b50608083015161133c60808601826110d5565b5060a083015161134f60a08601826110d5565b508091505092915050565b5f61136583836112cd565b905092915050565b5f602082019050919050565b5f611383826112a4565b61138d81856112ae565b93508360208202850161139f856112be565b805f5b858110156113da57848403895281516113bb858261135a565b94506113c68361136d565b925060208a019950506001810190506113a2565b50829750879550505050505092915050565b5f6020820190508181035f8301526114048184611379565b905092915050565b611415816111f8565b82525050565b5f60208201905061142e5f83018461140c565b92915050565b61143d81611271565b8114611447575f80fd5b50565b5f8135905061145881611434565b92915050565b5f806040838503121561147457611473610d9c565b5b5f61148185828601610dc3565b92505060206114928582860161144a565b9150509250929050565b5f602082840312156114b1576114b0610d9c565b5b5f6114be8482850161121f565b91505092915050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52602260045260245ffd5b5f600282049050600182168061150b57607f821691505b60208210810361151e5761151d6114c7565b5b50919050565b7f4d61782070726f706f73616c73207265616368656400000000000000000000005f82015250565b5f611558601583610e1b565b915061156382611524565b602082019050919050565b5f6020820190508181035f8301526115858161154c565b9050919050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52601160045260245ffd5b5f6115c382610da4565b91506115ce83610da4565b92508282026115dc81610da4565b915082820484148315176115f3576115f261158c565b5b5092915050565b5f61160482610da4565b915061160f83610da4565b92508282019050808211156116275761162661158c565b5b92915050565b5f819050815f5260205f209050919050565b5f6020601f8301049050919050565b5f82821b905092915050565b5f600883026116897fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8261164e565b611693868361164e565b95508019841693508086168417925050509392505050565b5f819050919050565b5f6116ce6116c96116c484610da4565b6116ab565b610da4565b9050919050565b5f819050919050565b6116e7836116b4565b6116fb6116f3826116d5565b84845461165a565b825550505050565b5f90565b61170f611703565b61171a8184846116de565b505050565b5b8181101561173d576117325f82611707565b600181019050611720565b5050565b601f821115611782576117538161162d565b61175c8461163f565b8101602085101561176b578190505b61177f6117778561163f565b83018261171f565b50505b505050565b5f82821c905092915050565b5f6117a25f1984600802611787565b1980831691505092915050565b5f6117ba8383611793565b9150826002028217905092915050565b6117d382610e11565b67ffffffffffffffff8111156117ec576117eb610f10565b5b6117f682546114f4565b611801828285611741565b5f60209050601f831160018114611832575f8415611820578287015190505b61182a85826117af565b865550611891565b601f1984166118408661162d565b5f5b8281101561186757848901518255600182019150602085019450602081019050611842565b868310156118845784890151611880601f891682611793565b8355505b6001600288020188555050505b505050505050565b5f6040820190506118ac5f830185610e02565b81810360208301526118be8184610e63565b90509392505050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52603260045260245ffd5b7f496e76616c69642070726f706f73616c204944000000000000000000000000005f82015250565b5f611928601383610e1b565b9150611933826118f4565b602082019050919050565b5f6020820190508181035f8301526119558161191c565b9050919050565b7f566f74696e6720706572696f64206861732065787069726564000000000000005f82015250565b5f611990601983610e1b565b915061199b8261195c565b602082019050919050565b5f6020820190508181035f8301526119bd81611984565b9050919050565b7f596f75206861766520616c726561647920766f746564000000000000000000005f82015250565b5f6119f8601683610e1b565b9150611a03826119c4565b602082019050919050565b5f6020820190508181035f830152611a25816119ec565b9050919050565b5f611a3682610da4565b91507fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8203611a6857611a6761158c565b5b600182019050919050565b5f604082019050611a865f830185610e02565b611a93602083018461127c565b9392505050565b7f4f776e61626c653a206e6577206f776e657220697320746865207a65726f20615f8201527f6464726573730000000000000000000000000000000000000000000000000000602082015250565b5f611af4602683610e1b565b9150611aff82611a9a565b604082019050919050565b5f6020820190508181035f830152611b2181611ae8565b9050919050565b7f4f776e61626c653a2063616c6c6572206973206e6f7420746865206f776e65725f82015250565b5f611b5c602083610e1b565b9150611b6782611b28565b602082019050919050565b5f6020820190508181035f830152611b8981611b50565b905091905056fea26469706673582212207879cc3866bd14086df6f39222b104dc5006b86f0f6497b1e296041b808f913b64736f6c63430008140033";

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

    public RemoteFunctionCall<TransactionReceipt> createProposal(String title, String description, BigInteger durationInDays) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_CREATEPROPOSAL, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(title), 
                new org.web3j.abi.datatypes.Utf8String(description), 
                new org.web3j.abi.datatypes.generated.Uint256(durationInDays)), 
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

    public RemoteFunctionCall<Tuple6<BigInteger, String, String, BigInteger, BigInteger, BigInteger>> proposals(BigInteger param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_PROPOSALS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple6<BigInteger, String, String, BigInteger, BigInteger, BigInteger>>(function,
                new Callable<Tuple6<BigInteger, String, String, BigInteger, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple6<BigInteger, String, String, BigInteger, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple6<BigInteger, String, String, BigInteger, BigInteger, BigInteger>(
                                (BigInteger) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (String) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue(), 
                                (BigInteger) results.get(4).getValue(), 
                                (BigInteger) results.get(5).getValue());
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
        public BigInteger id;

        public String title;

        public String description;

        public BigInteger votesFor;

        public BigInteger votesAgainst;

        public BigInteger expirationTime;

        public ProposalRaw(BigInteger id, String title, String description, BigInteger votesFor, BigInteger votesAgainst, BigInteger expirationTime) {
            super(new org.web3j.abi.datatypes.generated.Uint256(id), 
                    new org.web3j.abi.datatypes.Utf8String(title), 
                    new org.web3j.abi.datatypes.Utf8String(description), 
                    new org.web3j.abi.datatypes.generated.Uint256(votesFor), 
                    new org.web3j.abi.datatypes.generated.Uint256(votesAgainst), 
                    new org.web3j.abi.datatypes.generated.Uint256(expirationTime));
            this.id = id;
            this.title = title;
            this.description = description;
            this.votesFor = votesFor;
            this.votesAgainst = votesAgainst;
            this.expirationTime = expirationTime;
        }

        public ProposalRaw(Uint256 id, Utf8String title, Utf8String description, Uint256 votesFor, Uint256 votesAgainst, Uint256 expirationTime) {
            super(id, title, description, votesFor, votesAgainst, expirationTime);
            this.id = id.getValue();
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
