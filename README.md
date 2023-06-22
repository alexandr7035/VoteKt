# README.md

## Local setup
Use Android Emulator with default network settings

Deploy and test contract
```
# Run a node
mpx hardhat node

# Deploy contract
npx hardhat run --network goerli scripts/deploy.js

# (Optional) Interact from console
npx hardhat console --network localhost 
```

Compile contract and use via web3j
```
# Compile
solc --bin --abi contracts/VoteKtV01.sol --base-path '/' --include-path 'node_modules/' -o . --overwrite

# Generate Java wrapper
web3j generate solidity -b VotingContract.bin -a VotingContract.abi -o . -p .
```

Then copy Java class to project and clean-up

TODO: Gradle task for web3j

For localhost Gradle build variant, add following to `gradle.properties`:
```
# Address of localhost for Android emulator
LOCAL_NODE_URL="http://10.0.2.2:8545"

LOCAL_CONTRACT_ADDRESS="...HERE ADDRESS OF DEPLOYED CONTRACT ON LOCAL NODE..."

LOCAL_TEST_MNEMONIC="test test test test test test test test test test test junk"
LOCAL_TEST_ADDRESS="0x8626f6940E2eb28930eFb4CeF49B2d1F2C9C1199"
```