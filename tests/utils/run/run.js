const util = require('util');
const exec = util.promisify(require('child_process').exec);

const run = async (program, version, args = []) => {
    program = program.trim();
    version = version.trim();
    if (program && version) {
        const pathPrefix = './utils/run';
        return await exec(`bash ${pathPrefix}/run.sh "${program}" ${version} ${pathPrefix} ${args.join(' ')}`);
    }
    throw new Error('Need a program and path to compiler to run.');
};

module.exports = run;