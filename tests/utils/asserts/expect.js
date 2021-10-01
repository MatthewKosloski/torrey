const { expect: chaiExpect } = require('chai');
const { run } = require('../run');

const expect = async (program, version) => {
    let result = {
        stdout: '',
        stderr: ''
    };
    try {
        const { stdout } = await run(program, version);
        result.stdout = chaiExpect(stdout);
    } catch(err) {
        result.stderr = chaiExpect(err.stderr);
    }
    return result;
};

module.exports = expect;