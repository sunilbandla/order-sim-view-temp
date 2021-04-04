# Orders Simulation

## How to use this simulator

### Compile
`javac -cp ./src/gson-2.8.6.jar -d ./out ./src/sim/**/*.java ./src/sim/*.java`

### Run application
`java -cp ./out:./src/gson-2.8.6.jar sim.SimulationRunner 2`

- The `last` value in the above command configure the ingestion rate of orders per second.

### Run tests

- Add Junit5 to the project in `IntelliJ IDEA`
- Run tests in each `*Test.java` class

## Design

- `SimulationRunner` orchestrates the entire simulation. It starts order, kitchen and courier processor threads.
- Orders are generated at the configured rate by a background thread
- Orders are sent to the kitchen using a `BlockingQueue`
- `AtomicBoolean` is used to communicate to the kitchen that all order were generated
- Cooked orders are then placed in shelves (`BlockingQueue`s) based on their temperature
- If there's no room on a shelf, the overflow shelf is used
- The system checks if overflow orders can be moved back to their temperature-based shelf
- If no orders could be moved and the overflow shelf is full, the first item (likely close to being stale) on the shelf is removed
- After an item is shelved, a new thread is created to deliver the order
- Just before the courier arrives, stale orders are removed from the temperature-based and overflow shelf since couriers do not deliver stale orders 
- Courier removes order from shelf if it is available
