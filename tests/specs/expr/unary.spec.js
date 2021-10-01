const { expect: __expect } = require('../../utils');

const _expect = async (program) => {
    return __expect(program, '3.0.8');
}

describe.only('"(" "-" expr ")"', () => {
    it('Should error if no left parenthesis or child expression', async () => {
        const expect = await _expect(`
            -)
        `);
        expect.stderr.to.contain('Expected an integer, unary, binary, print, let, or identifier expression but found \'-\' instead');
        expect.stderr.to.contain('1 Error');
    });
    it('Should error if no left parenthesis', async () => {
        const expect = await _expect(`
            - 69)
        `);
        expect.stderr.to.contain('Expected an integer, unary, binary, print, let, or identifier expression but found \'-\' instead');
        expect.stderr.to.contain('1 Error');
    });
    it('Should error if no child expression', async () => {
        const expect = await _expect(`
            (-)
        `);
        expect.stderr.to.contain('Expected an integer, unary, binary, print, let, or identifier expression but found \')\' instead');
        expect.stderr.to.contain('1 Error');
    });
    it('Should error if no right parenthesis or child expression', async () => {
        const expect = await _expect(`
            (-
        `);
        expect.stderr.to.contain('Expected an integer, unary, binary, print, let, or identifier expression but found \'-\' instead');
        expect.stderr.to.contain('1 Error');
    });
    it('Should error if no right parenthesis', async () => {
        const expect = await _expect(`
            (- 420
        `);
        expect.stderr.to.contain('Expected a closing parenthesis \')\'');
        expect.stderr.to.contain('1 Error');
    });
    it('Should evaluate to zero if child expression is literal 0', async () => {
        const expect = await _expect(`
            (print (- 0))
        `);
        expect.stdout.to.equal('0');
    });
    it('Should error if child expression is literal true', async () => {
        const expect = await _expect(`
            (- true)
        `);
        expect.stderr.to.contain('Expected operand to operator \'-\' to be type \'INTEGER\' but found type \'BOOLEAN\' instead');
        expect.stderr.to.contain('1 Error');
    });
    it('Should error if child expression is literal false', async () => {
        const expect = await _expect(`
            (- false)
        `);
        expect.stderr.to.contain('Expected operand to operator \'-\' to be type \'INTEGER\' but found type \'BOOLEAN\' instead');
        expect.stderr.to.contain('1 Error');
    });
    it('Should evaluate to a positive integer if child expression is an integer literal nested within a unary expression', async () => {
        const expect = await _expect(`
            (println (- (- 0)) (- (- 1)))
        `);
        expect.stdout.to.equal('0\n1\n');
    });
    it('Should evaluate to a negative integer if child expression is an integer literal nested within a double unary expression', async () => {
        const expect = await _expect(`
            (print (- (- (- 25))))
        `);
        expect.stdout.to.equal('-25');
    });
    it('Should evaluate to zero if child expression is 0 nested in a double unary expression', async () => {
        const expect = await _expect(`
            (print (- (- (- 0))))
        `);
        expect.stdout.to.equal('0');
    });
    it('Should evaluate unary expressions of arbitrary depth', async () => {
        const expect = await _expect(`
            (println (- (- (- (- (- (- (- 3141519))))))))
            (println (- (- (- (- (- (- (- (- (- (- (- (- 6942069)))))))))))))
            (println (- (- (- (- (- (- (- (- (- (- (- (- (- (- (- (- (- (- (- 99999999))))))))))))))))))))
        `);
        expect.stdout.to.equal('-3141519\n6942069\n-99999999\n');
    });
});