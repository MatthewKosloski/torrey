const util = require('util');
const exec = util.promisify(require('child_process').exec);

const run = async (program, version, args = []) => {
    version = version.trim();
    if (version) {
        const pathPrefix = './utils/run';
        return await exec(`bash ${pathPrefix}/run.sh "${program}" ${version} ${pathPrefix} ${args.join(' ')}`);
    }
    throw new Error('Must specify compiler version to run.');
};

module.exports = run;