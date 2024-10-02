const fs = require("fs");

async function main() {
  const bytes = fs.readFileSync("2o1u.wasm");
  const env = { log: val => console.log(`logged ${val}`), };
  const { instance } = await WebAssembly.instantiate(
    bytes,
    {
      env: {
        log(val) {
          console.log(`log saw ${val}`);
        }
      }
    }
  );
  instance.exports.real_main();
  console.log(`finished`);
}
main();