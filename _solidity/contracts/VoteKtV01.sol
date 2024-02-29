pragma solidity ^0.8.0;

import "@openzeppelin/contracts/access/Ownable.sol";

contract VotingContract is Ownable {
    struct ProposalRaw {
        uint number;
        string uuid;
        string title;
        string description;
        uint votesFor;
        uint votesAgainst;
        uint256 expirationTime;
    }

    ProposalRaw[] public proposals;
    mapping(uint256 => mapping(address => bool)) public hasVoted;
    uint256 public maxProposals = 10;

    mapping(string => bool) private uuidExists;
    
    event ProposalCreated(uint256 proposalNumber, string title);
    event VoteCasted(uint256 proposalNumber, bool inFavor);
    event ProposalDeleted(uint256 proposalNumber);

    modifier onlyBeforeExpiration(uint256 proposalNumber) {
        require(
            block.timestamp < proposals[proposalNumber].expirationTime,
            "Voting period has expired"
        );
        _;
    }

    modifier onlyOnce(uint256 proposalNumber) {
        require(!hasVoted[proposalNumber][msg.sender], "You have already voted");
        _;
    }

    modifier checkActiveProposalsLimit() {
        require(proposals.length < maxProposals, "Max proposals reached");
        _;
    }

    function createProposal(
        string memory uuid,
        string memory title,
        string memory description,
        uint durationInDays
    )
        public
        onlyOwner
        checkActiveProposalsLimit
    {
        require(!uuidExists[uuid], "Proposal with this UUID already exists");

        uint256 proposalNumber = proposals.length;

        ProposalRaw memory newProposal;
        newProposal.number = proposalNumber;
        newProposal.title = title;
        newProposal.description = description;

        uint256 expiration = block.timestamp + durationInDays * 86400;
        newProposal.expirationTime = expiration;

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
