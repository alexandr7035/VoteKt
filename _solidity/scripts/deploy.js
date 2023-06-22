async function main () {
    // We get the contract to deploy
    const VotingContract = await ethers.getContractFactory('VotingContract');
    console.log('Deploying contract...');
    const contract = await VotingContract.deploy();
    await contract.deployed();
    console.log('Contract deployed to:', contract.address);
  }
  
  main()
    .then(() => process.exit(0))
    .catch(error => {
      console.error(error);
      process.exit(1);
    });