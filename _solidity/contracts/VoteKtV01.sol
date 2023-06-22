pragma solidity ^0.8.0;

import "@openzeppelin/contracts/access/Ownable.sol";

contract VotingContract is Ownable {
    struct ProposalRaw {
        string title;
        string description;
        uint votesFor;
        uint votesAgainst;
        uint256 expirationTime;
    }

    ProposalRaw[] public proposals;
    mapping(uint256 => mapping(address => bool)) public hasVoted;
    uint256 public maxProposals = 5;

    event ProposalCreated(uint256 proposalId, string title);
    event VoteCasted(uint256 proposalId, bool inFavor);
    event ProposalDeleted(uint256 proposalId);

    modifier onlyBeforeExpiration(uint256 proposalId) {
        require(
            block.timestamp < proposals[proposalId].expirationTime,
            "Voting period has expired"
        );
        _;
    }

    modifier onlyOnce(uint256 proposalId) {
        require(!hasVoted[proposalId][msg.sender], "You have already voted");
        _;
    }

    modifier checkActiveProposalsLimit() {
        require(proposals.length < maxProposals, "Max proposals reached");
        _;
    }

    function createProposal(string memory title, string memory description)
        public
        onlyOwner
        checkActiveProposalsLimit
    {
        uint256 proposalId = proposals.length;

        ProposalRaw memory newProposal;
        newProposal.title = title;
        newProposal.description = description;
        newProposal.expirationTime = block.timestamp + 1 days;

        proposals.push(newProposal);
        emit ProposalCreated(proposalId, title);
    }

    function deleteProposal(uint256 proposalId) public onlyOwner {
        require(proposalId < proposals.length, "Invalid proposal ID");

        delete proposals[proposalId];
        emit ProposalDeleted(proposalId);
    }

    function vote(uint256 proposalId, bool inFavor)
        public
        onlyBeforeExpiration(proposalId)
        onlyOnce(proposalId)
    {
        ProposalRaw storage proposal = proposals[proposalId];

        if (inFavor) {
            proposal.votesFor++;
        } else {
            proposal.votesAgainst++;
        }

        hasVoted[proposalId][msg.sender] = true;
        emit VoteCasted(proposalId, inFavor);
    }

    function getProposalsList() public view returns (ProposalRaw[] memory) {
        return proposals;
    }

    function getProposalDetails(uint256 proposalId)
        public
        view
        returns (ProposalRaw memory)
    {
        return proposals[proposalId];
    }
}
