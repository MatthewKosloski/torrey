const { expect: __expect } = require('../../utils');

const _expect = async (program) => {
    return __expect(program, '3.0.8');
}

describe('"(" "-" expr ")"', () => {
    it('Should be a syntax error if no left parenthesis and no operand', async () => {
        const expect = await _expect(`
            -)
        `);
        expect.stderr.to.contain('Expected an integer, unary, binary, print, let, or identifier expression but found \'-\' instead');
        expect.stderr.to.contain('1 Error');
    });
    it('Should be a syntax error if no left parenthesis', async () => {
        const expect = await _expect(`
            - 69)
        `);
        expect.stderr.to.contain('Expected an integer, unary, binary, print, let, or identifier expression but found \'-\' instead');
        expect.stderr.to.contain('1 Error');
    });
    it('Should be a syntax error if no operand', async () => {
        const expect = await _expect(`
            (-)
        `);
        expect.stderr.to.contain('Expected an integer, unary, binary, print, let, or identifier expression but found \')\' instead');
        expect.stderr.to.contain('1 Error');
    });
    it('Should be a syntax error if no right parenthesis and no operand', async () => {
        const expect = await _expect(`
            (-
        `);
        expect.stderr.to.contain('Expected an integer, unary, binary, print, let, or identifier expression but found \'-\' instead');
        expect.stderr.to.contain('1 Error');
    });
    it('Should be a syntax error if no right parenthesis', async () => {
        const expect = await _expect(`
            (- 420
        `);
        expect.stderr.to.contain('Expected a closing parenthesis \')\'');
        expect.stderr.to.contain('1 Error');
    });
    it('Should evaluate to zero if operand is literal 0', async () => {
        const expect = await _expect(`
            (print 
                (- 0))
        `);
        expect.stdout.to.equal('0');
    });
    it('Should be a semantic error if operand is literal true', async () => {
        const expect = await _expect(`
            (- true)
        `);
        expect.stderr.to.contain('Expected operand to operator \'-\' to be type \'INTEGER\' but found type \'BOOLEAN\' instead');
        expect.stderr.to.contain('1 Error');
    });
    it('Should be a semantic error if operand is literal false', async () => {
        const expect = await _expect(`
            (- false)
        `);
        expect.stderr.to.contain('Expected operand to operator \'-\' to be type \'INTEGER\' but found type \'BOOLEAN\' instead');
        expect.stderr.to.contain('1 Error');
    });
    it('Should evaluate to a positive integer if operand is an integer literal nested within a unary expression', async () => {
        const expect = await _expect(`
            (println 
                (- (- 0)) (- (- 1)))
        `);
        expect.stdout.to.equal('0\n1\n');
    });
    it('Should evaluate to a negative integer if operand is an integer literal nested within a double unary expression', async () => {
        const expect = await _expect(`
            (print 
                (- (- (- 25))))
        `);
        expect.stdout.to.equal('-25');
    });
    it('Should evaluate to zero if operand is 0 nested in a double unary expression', async () => {
        const expect = await _expect(`
            (print 
                (- (- (- 0))))
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
    it('Should accept an integer literal operand', async () => {
        const expect = await _expect(`
            (print 
                (- 42))
        `);
        expect.stdout.to.equal('-42');
    });
    it('Should accept an identifier operand', async () => {
        const expect = await _expect(`
            (print (let [a 400] 
                (- a)))
        `);
        expect.stdout.to.equal('-400');
    });
    it('Should accept a binary addition expression operand', async () => {
        const expect = await _expect(`
            (print 
                (- (+ 3 (- 5))))
        `);
        expect.stdout.to.equal('2');
    });
    it('Should accept a binary subtraction expression operand', async () => {
        const expect = await _expect(`
            (print 
                (- (- 420 69)))
        `);
        expect.stdout.to.equal('-351');
    });
    it('Should accept a binary multiplication expression operand', async () => {
        const expect = await _expect(`
            (print 
                (- (* 9 0)))
        `);
        expect.stdout.to.equal('0');
    });
    it('Should accept a binary division expression operand', async () => {
        const expect = await _expect(`
            (print 
                (- (/ 12 (- 5))))
        `);
        expect.stdout.to.equal('2');
    });
    // TODO: Should be a semantic error if the operand is an expression with operator ==
    // TODO: Should be a semantic error if the operand is an expression with operator <
    // TODO: Should be a semantic error if the operand is an expression with operator <=
    // TODO: Should be a semantic error if the operand is an expression with operator >
    // TODO: Should be a semantic error if the operand is an expression with operator >=
    it('Should be a semantic error if operand is a print expression', async () => {
        const expect = await _expect(`
            (print 
                (- (print 42)))
        `);
        expect.stderr.to.contain('Expected operand to operator \'-\' to be type \'INTEGER\' but found type \'NIL\' instead');
        expect.stderr.to.contain('1 Error');
    });
    it('Should be a semantic error if operand is a println expression', async () => {
        const expect = await _expect(`
            (print 
                (- (println 42)))
        `);
        expect.stderr.to.contain('Expected operand to operator \'-\' to be type \'INTEGER\' but found type \'NIL\' instead');
        expect.stderr.to.contain('1 Error');
    });
    it('Should accept a let expression', async () => {
        const expect = await _expect(`
            (print
                (- (let [] 42)))
        `);
        expect.stdout.to.equal('-42');
    });
    // TODO: Should accept not
    // TODO: Should accept and
    // TODO: Should accept or
    it('Should accept an if-then expression', async () => {
        const expect = await _expect(`
            (print 
                (- (if true 69420)))
        `);
        expect.stdout.to.equal('-69420');
    });
    it('Should accept an if-then-else expression', async () => {
        const expect = await _expect(`
            (print 
                (- (if false 0 1)))
        `);
        expect.stdout.to.equal('-1');
    });
});