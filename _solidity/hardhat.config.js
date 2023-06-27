require('@nomiclabs/hardhat-ethers');
require('dotenv').config()


module.exports = {
  solidity: "0.8.18",
    networks: {
     goerli: {
       url: `https://goerli.infura.io/v3/${process.env.INFURA_API_KEY}`,
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
