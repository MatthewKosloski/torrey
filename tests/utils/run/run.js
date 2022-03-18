const util = require('util');
const exec = util.promisify(require('child_process').exec);

const run = async (program, version, args = []) => {
    version = version.trim();
    if (version) {
        return await exec(`bash ./utils/run/run.sh "${program}" ${version} ${args.join(' ')}`);
    }
};

module.exports = run;
