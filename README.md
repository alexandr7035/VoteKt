# README.md

## Running Android app
### Configure gradle
Add following to `gradle.properties`:
```
# Sepolia conig
TESTNET_TEST_MNEMONIC="..."
TESTNET_CONTRACT_ADDRESS="<DEPLOYED_CONTRACT_ADDRESS>"
TESTNET_CHAIN_ID=11155111

INFURA_API_KEY="<INFURA_API_KEY>"
INFURA_NODE_URL="https://sepolia.infura.io"
INFURA_WSS_NODE_URL="wss://sepolia.infura.io:443/ws/v3/<INFURA_API_KEY>"

# Hardhat config
LOCAL_CONTRACT_ADDRESS="0x5FbDB2315678afecb367f032d93F642f64180aa3"
LOCAL_TEST_MNEMONIC="test test test test test test test test test test test junk"
LOCAL_CHAIN_ID=31337

LOCAL_NODE_URL="http://10.0.2.2:8545"
LOCAL_WSS_NODE_URL="ws://10.0.2.2:8545"

ENABLE_DEMO_NODE=true
```

If you don't need `demo` mode, you can leave `LOCAL_TEST_MNEMONIC` and `TESTNET_TEST_MNEMONIC` blank.

### Run app with local env
- Run HardHat node locally
```shell
mpx hardhat node
npx hardhat compile
npx hardhat --network localhost deploy
```
- Choose `local*` build variant and build the app
- Use Android Emulator with default network settings

### Run app with Sepolia testnet
[Create](https://docs.infura.io/dashboard/create-api) an Infura API key  
Choose `infura*` build variant and build the app.  
App will run on [Sepolia](https://sepolia.etherscan.io/) Testnet

## Development

### Setup Hardhat environment
```
# Setup .env file. Use test mnemonic here
# Add Infura API key if you need operate on testnet
MNEMONIC="test test test test test test test test test test test junk"
INFURA_API_KEY="...."

# Run a node
mpx hardhat node

# Deploy contract
# (Sometimes may not compile automatically)
npx hardhat compile
npx hardhat --network localhost deploy

# Create random proposal
npx hardhat --network localhost create_proposal

# Spawn fake votes
npx hardhat --network localhost spawn_votes

# (Optional) Interact from console
npx hardhat --network localhost console

# (Opional) Subscribe to contract events via websockets (replace address with actual)
npx wscat -c http://127.0.0.1:8545/  
>  {"jsonrpc":"2.0","id":1,"method":"eth_subscribe","params":["logs",{"address":"<CONTRACT_ADDRESS>","topics":[]}]}
```


## Useful materials
[Blockchain Series — How MetaMask Creates Accounts](https://levelup.gitconnected.com/blockchain-series-how-metamask-creates-accounts-a8971b21a74b)

[Build your first Android Dapp using Web3j and Infura](https://medium.com/@madhurakunjir2611/build-your-first-android-dapp-using-web3j-and-infura-36d2596c1e2a#:~:text=The%20default%20derivation%20path%20used,account%20into%20the%20Credentials%20object.)

[Ethereum Unit Converter](https://eth-converter.com/)

[Infura Docs](https://docs.infura.io/api/networks/ethereum/how-to)

[Understanding Ethereum Transaction Statuses](https://medium.com/@nicholaschn/understanding-ethereum-transaction-statuses-5c505b2b123b)

[How to use Time in a Smart Contract?](https://medium.com/coinmonks/how-to-use-time-in-a-smart-contract-1c8d063b6a2b)

[Ethereum RPCs, Methods, and Calls](https://dzone.com/articles/ethereum-rpcs-methods-and-calls)

[Subscribing to smart contract events with web3.js](https://support.chainstack.com/hc/en-us/articles/4403518123161-Subscribing-to-smart-contract-events-with-web3-js)

[Understanding event logs on the Ethereum blockchain](https://medium.com/mycrypto/understanding-event-logs-on-the-ethereum-blockchain-f4ae7ba50378)

[The Magic of Digital Signatures on Ethereum](https://medium.com/mycrypto/the-magic-of-digital-signatures-on-ethereum-98fe184dc9c7)

[Intro to Encoding & Decoding Ethereum Contract Functions](https://joshua-data.medium.com/intro-to-encoding-and-decoding-ethereum-contract-functions-0e12583916aa)  

[Goerli’s Deprecation: Navigating the Transition to Sepolia](https://medium.com/buildbear/goerlis-deprecation-navigating-the-transition-to-sepolia-84ea374bed9a#:~:text=We're%20here%20to%20announce,by%20the%20Ethereum%20Foundation%20team.)  
[Deprecation Notice for all Infura-Supported Goerli Endpoints](https://www.infura.io/blog/post/deprecation-notice-for-all-infura-supported-goerli-endpoints)

[Exploring the Differences Between Payable and Non-Payable Functions in Solidity: An In-Depth Analysis](https://medium.com/coinmonks/exploring-the-differences-between-payable-and-non-payable-functions-in-solidity-an-in-depth-d031c6ae577b#:~:text=In%20Solidity%2C%20a%20payable%20function,balance%E2%80%9D%20property.)

[Why are Encoders used in Solidity?](https://medium.com/coinmonks/why-are-encoders-used-in-solidity-6930e913ce48)

[Are Ethereum Full Nodes Really Full? An Experiment](https://medium.com/@marcandrdumas/are-ethereum-full-nodes-really-full-an-experiment-b77acd086ca7#:~:text=Truth%20is%2C%20a%20specific%20type,the%20space%2C%20about%20180%20GB.)

[Explaining Ethereum Contract ABI & EVM Bytecode](https://medium.com/@eiki1212/explaining-ethereum-contract-abi-evm-bytecode-6afa6e917c3b)

## Tools
[Ethereum Raw Transaction Decoder](https://rawtxdecode.in/)  
[Online Ethereum Abi encoder and decoder](https://adibas03.github.io/online-ethereum-abi-encoder-decoder/#/)  

[Online ABI Encoder](https://abi.hashex.org/)  
[Ethereum input data decoder](https://lab.miguelmota.com/ethereum-input-data-decoder/example/)  
[How to Set EIP-1559 Gas Fees?](https://medium.com/imtoken/how-to-set-eip-1559-gas-fees-3ea9b9f16242)  

[Crypto Logos](https://cryptologos.cc/)

## Notes
Infura limits (Free):  
100,000 Total Requests/Day  
25,000 Ethereum Mainnet Archive Requests/Day  


## DEPRECATED

### Compile contract to use via web3j
Install web3j cli tool if not installed
```shell
curl -L get.web3j.io | sh && source ~/.web3j/source.sh
```

Install web3j cli tool if not installed
```shell
curl -L get.web3j.io | sh && source ~/.web3j/source.sh
```

Compile contract with `solc` and use `web3j` cli to generate Java class
```shell
# Compile
solc --bin --abi contracts/VoteKtV01.sol --base-path '/' --include-path 'node_modules/' -o . --overwrite

# Generate Java wrapper
web3j generate solidity -b VotingContract.bin -a VotingContract.abi -o . -p .
```

Then copy Java class to project and clean-up.

**TODO**: Gradle task for web3j