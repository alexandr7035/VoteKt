// SPDX-License-Identifier: GPL-3.0
pragma solidity >= 0.4.16 < 0.9.0;

contract VoteKtV01 {

    Proposal[] public proposals;

    struct Proposal {
        uint id;
        string title;
        string desc;
        uint votesFor;
        uint votesAgainst;
        uint votingEnds;
    }

    function createProposal(string memory _title, string memory _desc) public {
        
        uint proposalId = proposals.length;
        uint votingEnds = block.timestamp + (3 days);

        Proposal memory newProposal = Proposal({
            id: proposalId,
            title: _title,
            desc: _desc,
            votesFor: 0,
            votesAgainst: 0,
            votingEnds: votingEnds
        });

        proposals.push(newProposal);
    }

    function getProposals() public view returns (Proposal[] memory) {
        return proposals;
    }

    function vote(bool _isFor, uint proposalId) public {
        // require(block.timestamp < proposals[proposalId - 1].votingEnds, "Voting has ended");

        Proposal storage proposal = proposals[proposalId];

        if (_isFor) {
            proposal.votesFor++;
        }
        else {
            proposal.votesAgainst++;
        }
    }
}