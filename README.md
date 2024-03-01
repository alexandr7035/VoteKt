# README.md

## Notes
Infura limits (Free):  
100,000 Total Requests/Day  
25,000 Ethereum Mainnet Archive Requests/Day  

[Dashboard | Create a key](https://app.infura.io/)  
[See all info about limits](https://support.infura.io/hc/en-us/articles/10650822554395-What-are-Infura-s-daily-limits-)  
[Blockchain Series — How MetaMask Creates Accounts](https://levelup.gitconnected.com/blockchain-series-how-metamask-creates-accounts-a8971b21a74b)

## Running the app

### Local setup
Use Android Emulator with default network settings

### Deploy and test contract
```
# Setup env
MNEMONIC="fix tape two tooth country bottom siren decide east salad music warfare"
INFURA_API_KEY="...."

# Run a node
mpx hardhat node

# Deploy contract
npx hardhat run --network localhost scripts/deploy.js

# (Optional) When using local node, we can prepare mock proposals and votes
npx hardhat run --network localhost scripts/mocks/mock_local_votings.js

# (Optional) Interact from console
npx hardhat console --network localhost 
```

### Compile contract to use via web3j
Install web3j cli tool if not installed
```
curl -L get.web3j.io | sh && source ~/.web3j/source.sh
```

Compile contract with solc and use web3j cli to generate Java class
```
# Compile
solc --bin --abi contracts/VoteKtV01.sol --base-path '/' --include-path 'node_modules/' -o . --overwrite

# Generate Java wrapper
web3j generate solidity -b VotingContract.bin -a VotingContract.abi -o . -p .
```

Then copy Java class to project and clean-up

**TODO**: Gradle task for web3j

### Run Android app with local node
For localhost Gradle build variant, add following to `gradle.properties`:
```
# Address of localhost for Android emulator
LOCAL_NODE_URL="http://10.0.2.2:8545"

LOCAL_CONTRACT_ADDRESS="...HERE ADDRESS OF DEPLOYED CONTRACT ON LOCAL NODE..."

LOCAL_TEST_MNEMONIC="test test test test test test test test test test test junk"
LOCAL_TEST_ADDRESS="0x8626f6940E2eb28930eFb4CeF49B2d1F2C9C1199"
```

Choose localDebug build variant and build

## Useful materials
[Build your first Android Dapp using Web3j and Infura](https://medium.com/@madhurakunjir2611/build-your-first-android-dapp-using-web3j-and-infura-36d2596c1e2a#:~:text=The%20default%20derivation%20path%20used,account%20into%20the%20Credentials%20object.)

[Ethereum Unit Converter](https://eth-converter.com/)

[Infura Docs](https://docs.infura.io/api/networks/ethereum/how-to)

[Understanding Ethereum Transaction Statuses](https://medium.com/@nicholaschn/understanding-ethereum-transaction-statuses-5c505b2b123b)

[How to use Time in a Smart Contract?](https://medium.com/coinmonks/how-to-use-time-in-a-smart-contract-1c8d063b6a2b)

[Ethereum RPCs, Methods, and Calls](https://dzone.com/articles/ethereum-rpcs-methods-and-calls)
