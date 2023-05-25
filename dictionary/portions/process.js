const fs = require("fs")

// read portions

const portions = new Map()

fs.readdirSync("./dictionary/").forEach(file => {
    fs.readFileSync("./dictionary/" + file).toString().replaceAll("\r\n", "\n").split("\n").forEach(line => {
        if (!(!line || line.startsWith("#") || line.startsWith("//"))) {

            const portion = line.split(":")[line.split(":").length - 1]
            const vocabulary = line.substring(0, line.length - (portion.length+1)).trim()

            const vocabularies = portions.get(portion) ?? []
            vocabularies.push(vocabulary)
            portions.set(portion, vocabularies)
            
        }
    })
})

// create folder

if (fs.existsSync("./out/portions")) {
    fs.rmSync("./out/portions", {recursive: true})
}
fs.mkdirSync("./out/portions")

// output to files

portions.forEach( (vocabularies, portion) => {
  fs.writeFileSync("./out/portions/" + portion + ".txt", vocabularies.join("\n"))
})

const allPortions = [];
portions.forEach( (vocabulary, portion) => {
  allPortions.push({
    name: portion,
    file: portion + ".txt"
  })
})
fs.writeFileSync("./out/portions.json", JSON.stringify({
  portions: allPortions
}, null, 2))