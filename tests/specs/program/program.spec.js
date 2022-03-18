const { expect: __expect } = require('../../utils');

const _expect = async (program) => {
    return __expect(program, '3.0.8');
}

describe('program', () => {
    it('Should accept an empty program', async () => {
        const expect = await _expect('');
        expect.stdout.to.equal('');
    });
    it('Should accept a white space program', async () => {
        const expect = await _expect(' \n\n  \n\n');
        expect.stdout.to.equal('');
    });
    it('Should accept exactly one expression', async () => {
        const expect = await _expect('(println 42)');
        expect.stdout.to.equal('42\n');
    });
    it('Should accept more than one expression', async () => {
        const expect = await _expect(`
            (let [a 5]
                (+ 2 a))
            (println (let [a 2]
                (- 2)))
        `);
        expect.stdout.to.equal('-2\n');
    });
});
