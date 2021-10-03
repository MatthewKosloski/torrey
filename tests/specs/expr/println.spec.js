const { expect: __expect } = require('../../utils');

const _expect = async (program) => {
    return __expect(program, '3.0.8');
}

describe('"(" "println" expr+ ")"', () => {
    it('Should be a syntax error if no left parenthesis and no operand', async () => {
        const expect = await _expect(`
            println)
        `);
        expect.stderr.to.contain('Expected an integer, unary, binary, print, let, or identifier expression but found \'println\' instead');
        expect.stderr.to.contain('1 Error');
    });
    it('Should be a syntax error if no left parenthesis', async () => {
        const expect = await _expect(`
            println 69)
        `);
        expect.stderr.to.contain('Expected an integer, unary, binary, print, let, or identifier expression but found \'println\' instead');
        expect.stderr.to.contain('1 Error');
    });
    it('Should be a syntax error if no operand', async () => {
        const expect = await _expect(`
            (println)
        `);
        expect.stderr.to.contain('Expected an integer, unary, binary, print, let, or identifier expression but found \')\' instead');
        expect.stderr.to.contain('1 Error');
    });
    it('Should be a syntax error if no right parenthesis and no operand', async () => {
        const expect = await _expect(`
            (println
        `);
        expect.stderr.to.contain('Expected an integer, unary, binary, print, let, or identifier expression but found \'println\' instead');
        expect.stderr.to.contain('1 Error');
    });
    it('Should be a syntax error if no right parenthesis', async () => {
        const expect = await _expect(`
            (println 420
        `);
        expect.stderr.to.contain('Expected a closing parenthesis \')\'');
        expect.stderr.to.contain('1 Error');
    });
    it('Should accept an integer literal operand', async () => {
        const expect = await _expect(`
            (println 3)
        `);
        expect.stdout.to.equal('3\n');
    });
    it('Should accept more than one integer literal operands', async () => {
        const expect = await _expect(`
            (println 1 2 3)
        `);
        expect.stdout.to.equal('1\n2\n3\n');
    });
    // TODO: Should accept a boolean literal true operand
    // TODO: Should accept more than one boolean literal true operands
    // TODO: Should accept a boolean literal false operand
    // TODO: Should accept more than one boolean literal false operands
    it('Should accept an identifier operand', async () => {
        const expect = await _expect(`
            (let [a 4]
                (println a))
        `);
        expect.stdout.to.equal('4\n');
    });
    it('Should accept more than one identifier operands', async () => {
        const expect = await _expect(`
            (let [a 4 b 100]
                (println a b))
        `);
        expect.stdout.to.equal('4\n100\n');
    });
    it('Should accept a unary operand', async () => {
        const expect = await _expect(`
            (println (- 1))
        `);
        expect.stdout.to.equal('-1\n');
    });
    it('Should accept more than one unary operands', async () => {
        const expect = await _expect(`
            (println (- 1) (- 10) (- 999))
        `);
        expect.stdout.to.equal('-1\n-10\n-999\n');
    });
    it('Should accept a binary addition operand', async () => {
        const expect = await _expect(`
            (println (+ 2 3))
        `);
        expect.stdout.to.equal('5\n');
    });
    it('Should accept more than one binary addition operands', async () => {
        const expect = await _expect(`
            (println (+ 1 3) (+ 2 (- 3)))
        `);
        expect.stdout.to.equal('4\n-1\n');
    });
    it('Should accept a binary subtraction operand', async () => {
        const expect = await _expect(`
            (println (- 2 3))
        `);
        expect.stdout.to.equal('-1\n');
    });
    it('Should accept more than one binary subtraction operands', async () => {
        const expect = await _expect(`
            (println (- 2 3) (- 5 3))
        `);
        expect.stdout.to.equal('-1\n2\n');
    });
    it('Should accept a binary multiplication operand', async () => {
        const expect = await _expect(`
            (println (* 100 (- 3)))
        `);
        expect.stdout.to.equal('-300\n');
    });
    it('Should accept more than one binary multiplication operands', async () => {
        const expect = await _expect(`
            (println (* 5 (- 9)) (* 8 3))
        `);
        expect.stdout.to.equal('-45\n24\n');
    });
    it('Should accept a binary division operand', async () => {
        const expect = await _expect(`
            (println (/ 5 2))
        `);
        expect.stdout.to.equal('2\n');
    });
    it('Should accept more than one binary division operands', async () => {
        const expect = await _expect(`
            (println (/ 2 20) (/ (- 16) 4))
        `);
        expect.stdout.to.equal('0\n-4\n');
    });
    // TODO: Should accept ==
    // TODO: Should accept more than one ==
    // TODO: Should accept <
    // TODO: Should accept more than one <
    // TODO: Should accept <=
    // TODO: Should accept more than one <=
    // TODO: Should accept >
    // TODO: Should accept more than one >
    // TODO: Should accept >=
    // TODO: Should accept more than one >=
    it('Should be a semantic error if operand is a print expression', async () => {
        const expect = await _expect(`
            (println (print 42))
        `);
        expect.stderr.to.contain('Cannot print operand \'print\' because it does not evaluate to a known type');
        expect.stderr.to.contain('1 Error');
    });
    it('Should be a semantic error if operands are print expressions', async () => {
        const expect = await _expect(`
            (println (print 42) (print (- 42)))
        `);
        expect.stderr.to.contain('Cannot print operand \'print\' because it does not evaluate to a known type');
        expect.stderr.to.contain('2 Errors');
    });
    it('Should be a semantic error if operand is a println expression', async () => {
        const expect = await _expect(`
            (println (println 42))
        `);
        expect.stderr.to.contain('Cannot print operand \'println\' because it does not evaluate to a known type');
        expect.stderr.to.contain('1 Error');
    });
    it('Should be a semantic error if operands are println expressions', async () => {
        const expect = await _expect(`
            (println (println 42) (println (- 42)))
        `);
        expect.stderr.to.contain('Cannot print operand \'println\' because it does not evaluate to a known type');
        expect.stderr.to.contain('2 Errors');
    });
    it('Should accept a let expression', async () => {
        const expect = await _expect(`
            (println (let [] 42))
        `);
        expect.stdout.to.equal('42\n');
    });
    // TODO: Should accept not
    // TODO: Should accept and
    // TODO: Should accept or
    it('Should accept an if-then expression', async () => {
        const expect = await _expect(`
            (println (if true 69420))
        `);
        expect.stdout.to.equal('69420\n');
    });
    it('Should accept an if-then-else expression', async () => {
        const expect = await _expect(`
            (println (if false 0 1))
        `);
        expect.stdout.to.equal('1\n');
    });
});