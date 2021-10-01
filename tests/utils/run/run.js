const { spawn } = require('child_process');
const util = require('util');
const exec = util.promisify(require('child_process').exec);

const _spawn = async (cmd, args) => {
    return new Promise((resolve, reject) => {
        const proc = spawn(cmd, args);

        let result = {
            stdout: Buffer.alloc(0),
            stderr: Buffer.alloc(0),
            exitCode: 0
        };

        proc.stdout.on('data', (buff) => result.stdout += buff);
        proc.stderr.on('data', (buff) => result.stderr += buff);
    
        proc.on('close', (exitCode) => {
            result.exitCode = exitCode;
            resolve(result);
        });

        proc.on('error', () => {
            reject(new Error(`Encountered an error when running ${cmd}`));
        })
    });
}

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