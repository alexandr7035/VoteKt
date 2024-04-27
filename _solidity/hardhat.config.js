require('@nomiclabs/hardhat-ethers');
require('dotenv').config()
const { makeMockVotes } = require('./scripts/spawn_votes.js');
const { deployContract } = require('./scripts/deploy.js');
const { createProposal } = require('./scripts/create_proposal.js');

task(
  "spawn_votes",
  "Mocks votes on specified proposal",
  async function (taskArguments, hre, runSuper) {
    await makeMockVotes(taskArguments.proposalNumber)
  }
)
.addPositionalParam("proposalNumber", "The number of proposal (#X)");

task(
  "deploy",
  "Deploy VoteKt contract",
  async function (taskArguments, hre, runSuper) {
    await deployContract()
  }
)

task(
  "create_proposal",
  "Create random proposal",
  async function (taskArguments, hre, runSuper) {
    await createProposal()
  }
)

module.exports = {
  solidity: "0.8.18",
    networks: {
     sepolia: {
       url: `https://sepolia.infura.io/v3/${process.env.INFURA_API_KEY}`,
       accounts: { mnemonic: process.env.MNEMONIC },
     },

    hardhat: {
      blockTime: 3000,
      mining: {
        auto: false,
        interval: 3000
      }
    },
   },
};
