async function createProposal() {
  const VotingContract = await ethers.getContractFactory('VotingContract');
  const contract = await VotingContract.attach('0x5FbDB2315678afecb367f032d93F642f64180aa3');

  const proposal = await getRandomProposal();
  await contract.createProposal(`uuid${Math.random() * 100}`, proposal.title, proposal.description, 1);
  console.log(`Created proposal ${proposal.title}`)
}

async function getRandomProposal() {
  const mockProposals = [
      { title: "Green Spaces Initiative", description: "Implementing a city-wide plan to create more green spaces, parks, and urban gardens to improve air quality, provide recreational areas, and promote biodiversity." },
      { title: "Public Art Installation Project", description: "Launching a program to commission and install public art pieces throughout the city to beautify neighborhoods, inspire creativity, and foster community engagement." },
      { title: "Tech Education for Seniors", description: "Introducing technology workshops tailored for senior citizens to help them learn basic digital skills, navigate the internet, and stay connected with loved ones in today's digital age." },
      { title: "Community Food Co-op", description: "Establishing a cooperative grocery store owned and operated by local residents, offering fresh produce, bulk goods, and community events to promote food security and sustainability." },
      { title: "Youth Mental Health Support Program", description: "Launching a comprehensive mental health program in schools to provide counseling services, peer support groups, and educational resources to address the growing mental health needs of young people." }
  ];
  
  const randomIndex = Math.floor(Math.random() * mockProposals.length);
  return mockProposals[randomIndex];
}

module.exports = {
  createProposal
}

