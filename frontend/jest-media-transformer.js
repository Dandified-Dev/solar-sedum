module.exports = {
    process(src, sourcePath) {
        return `module.exports = ${JSON.stringify(sourcePath)};`
    }
};