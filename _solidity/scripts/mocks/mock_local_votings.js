// Index = proposal id now
let proposalsCount = []

async function main () {
    await createMockProposals();
    await makeMockVotes();
  }

  async function createMockProposals() {
    const VotingContract = await ethers.getContractFactory('VotingContract');
    const c = await VotingContract.attach('0x5FbDB2315678afecb367f032d93F642f64180aa3');

    // Mock proposal data
    const mockProposals = [
        { title: "Proposal 1", description: "Description 1" },
        { title: "Proposal 2", description: "Description 2" },
        { title: "Proposal 3", description: "Description 3" },
    ];

    for (const proposal of mockProposals) {
        const randomDayCount = Math.floor(Math.random() * 10) + 1;
        const { title, description } = proposal;
        await c.createProposal(title, description, randomDayCount);
        proposalsCount++;
    }
}

  async function makeMockVotes() {
    // Mock hardhat mnemonics
    const wallets = [
        "0x689af8efa8c651a91ad287602527f3af2fe9f6501a7ac4b061667b5a93e037fd",
        "0xc526ee95bf44d8fc405a158bb884d9d1238d99f0612e9f33d006bb0789009aaa",
        "0x47c99abed3324a2707c28affff1267e45918ec8c3f20b8aa892e8b065d2942dd",
        "0xa267530f49f8280200edf313ee7af6b827f2a8bce2897751d06a843f644967b1",
        "0xf214f2b2cd398c806f84e317254e0f0b801d0643303237d97a22a48e01628897",
        "0x92db14e403b83dfe3df233f83dfa3a0d7096f21ca9b0d6d6b8d88b2b4ec1564e",
        "0x47e179ec197488593b187f80a00eb0da91f1b9d0b13f8733639f19c30a34926a",
        "0x7c852118294e51e653712a81e05800f419141751be58f605c371e15141b007a6",
        "0x5de4111afa1a4b94908f83103eb1f1706367c2e68ca870fc3fb9a804cdab365a",
    ];

    for (const privateKey of wallets) {
        console.log(`Mocking votes for ${privateKey}`)
        const wallet = new ethers.Wallet(privateKey, ethers.provider);
        const VotingContract = await ethers.getContractFactory("VotingContract", wallet);
        const contract = await VotingContract.attach("0x5FbDB2315678afecb367f032d93F642f64180aa3");

        // Vote for each proposal
        for (let i = 0; i <= proposalsCount-1; i++) {
            // Voting logic, replace the placeholder values with your actual voting parameters
            const proposalId = i;
            const support = Math.random() < 0.5;

            console.log(`Voting on proposal ${proposalId} with vote ${support}`)

            // Call the vote function
            await contract.vote(proposalId, support);
        }
    }
}
  
  main()
    .then(() => process.exit(0))
    .catch(error => {
      console.error(error);
      process.exit(1);
    });
