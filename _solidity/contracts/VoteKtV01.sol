pragma solidity ^0.8.0;

import "@openzeppelin/contracts/access/Ownable.sol";

contract VotingContract is Ownable {
    uint256 public constant MAX_PROPOSAL_TITLE_LENGTH = 100;
    uint256 public constant MAX_PROPOSAL_DESCRIPTION_LENGTH = 500;
    uint256 public constant NIN_PROPOSAL_DURATION = 1 hours;
    uint256 public constant MAX_PROPOSAL_DURATION = 365 days;
    uint public constant MAX_PROPOSAL_COUNT = 50;

    uint256 public CREATE_PROPOSAL_FEE = 0.025 ether;

    struct ProposalRaw {
        uint number;
        string uuid;
        string title;
        string description;
        uint votesFor;
        uint votesAgainst;
        address creatorAddress;
        uint256 creationTimeMills;
        uint256 expirationTimeMills;
    }

    ProposalRaw[] public proposals;
    mapping(uint256 => mapping(address => bool)) public hasVoted;

    mapping(string => bool) private uuidExists;
    
    event ProposalCreated(uint256 proposalNumber, string title);
    event VoteCasted(uint256 proposalNumber, bool inFavor);
    event ProposalDeleted(uint256 proposalNumber);

    modifier onlyBeforeExpiration(uint256 proposalNumber) {
        require(
            block.timestamp < proposals[proposalNumber].expirationTimeMills,
            "Voting period has expired"
        );
        _;
    }

    modifier onlyOnce(uint256 proposalNumber) {
        require(!hasVoted[proposalNumber][msg.sender], "You have already voted");
        _;
    }

    modifier checkActiveProposalsLimit() {
        require(proposals.length < MAX_PROPOSAL_COUNT, "Max proposals reached");
        _;
    }

    function createProposal(
        string memory uuid,
        string memory title,
        string memory description,
        uint durationInHours
    )
        public payable
        checkActiveProposalsLimit
    {
        require(!uuidExists[uuid], "Proposal with this UUID already exists");
        require(msg.value >= CREATE_PROPOSAL_FEE, "Insufficient fee to create proposal");
        require(durationInHours * 3600 >= NIN_PROPOSAL_DURATION, "Duration is less than minimal");
        require(durationInHours * 3600 <= MAX_PROPOSAL_DURATION, "Duration is greater than max");

        uint256 proposalNumber = proposals.length;

        ProposalRaw memory newProposal;
        newProposal.number = proposalNumber;
        newProposal.title = title;
        newProposal.description = description;
        newProposal.uuid = uuid;
        newProposal.creationTimeMills = block.timestamp;
        newProposal.creatorAddress = msg.sender;

        uint256 expiration = block.timestamp + durationInHours * 3600;

        newProposal.expirationTimeMills = expiration;

        proposals.push(newProposal);
        emit ProposalCreated(proposalNumber, title);

        uuidExists[uuid] = true;
    }

    function deleteProposal(uint256 proposalNumber) public onlyOwner {
        require(proposalNumber < proposals.length, "Invalid proposal number");

        delete proposals[proposalNumber];
        emit ProposalDeleted(proposalNumber);
    }

    function vote(uint256 proposalNumber, bool inFavor)
        public
        onlyBeforeExpiration(proposalNumber)
        onlyOnce(proposalNumber)
    {
        ProposalRaw storage proposal = proposals[proposalNumber];

        if (inFavor) {
            proposal.votesFor++;
        } else {
            proposal.votesAgainst++;
        }

        hasVoted[proposalNumber][msg.sender] = true;
        emit VoteCasted(proposalNumber, inFavor);
    }

    function getProposalsList() public view returns (ProposalRaw[] memory) {
        return proposals;
    }

    function getProposalDetails(uint256 proposalNumber)
        public
        view
        returns (ProposalRaw memory)
    {
        return proposals[proposalNumber];
    }
}
