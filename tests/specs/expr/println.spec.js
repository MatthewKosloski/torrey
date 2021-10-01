const { expect: __expect } = require('../../utils');

const _expect = async (program) => {
    return __expect(program, '3.0.8');
}

describe('"(" "println" expr+ ")"', () => {
    it('Should error if no left parenthesis', async () => {
        const expect = await _expect(`
            println)
        `);
        expect.stderr.to.contain('Expected an integer, unary, binary, print, let, or identifier expression but found \'println\' instead');
        expect.stderr.to.contain('1 Error');
    });
    it('Should error if no child expressions', async () => {
        const expect = await _expect(`
            (println)
        `);
        expect.stderr.to.contain('Expected an integer, unary, binary, print, let, or identifier expression but found \')\' instead');
        expect.stderr.to.contain('1 Error');
    });
    it('Should error if no right parenthesis', async () => {
        const expect = await _expect(`
            (println
        `);
        expect.stderr.to.contain('Expected an integer, unary, binary, print, let, or identifier expression but found \'println\' instead');
        expect.stderr.to.contain('1 Error');
    });
    it('Should print an integer literal expression', async () => {
        const expect = await _expect(`
            (println 3)
        `);
        expect.stdout.to.equal('3\n');
    });
    it('Should print more than one integer literal expression', async () => {
        const expect = await _expect(`
            (println 1 2 3)
        `);
        expect.stdout.to.equal('1\n2\n3\n');
    });
    // TODO: Should be able to print boolean literal true
    // TODO: Should be able to print more than one boolean literal true
    // TODO: Should be able to print boolean literal false
    // TODO: Should be able to print more than one boolean literal false
    it('Should print an identifier expression', async () => {
        const expect = await _expect(`
            (let [a 4]
                (println a))
        `);
        expect.stdout.to.equal('4\n');
    });
    it('Should print more than one identifier expression', async () => {
        const expect = await _expect(`
            (let [a 4 b 100]
                (println a a b))
        `);
        expect.stdout.to.equal('4\n4\n100\n');
    });
    it('Should print a unary expression', async () => {
        const expect = await _expect(`
            (println (- 1))
        `);
        expect.stdout.to.equal('-1\n');
    });
    it('Should print more than one unary expression', async () => {
        const expect = await _expect(`
            (println (- 1) (- 10) (- 999))
        `);
        expect.stdout.to.equal('-1\n-10\n-999\n');
    });
    it('Should print a binary addition expression', async () => {
        const expect = await _expect(`
            (println (+ 2 3))
        `);
        expect.stdout.to.equal('5\n');
    });
    it('Should print more than one binary addition expression', async () => {
        const expect = await _expect(`
            (println (+ 1 3) (+ 2 (- 3)))
        `);
        expect.stdout.to.equal('4\n-1\n');
    });
    it('Should print a binary subtraction expression', async () => {
        const expect = await _expect(`
            (println (- 2 3))
        `);
        expect.stdout.to.equal('-1\n');
    });
    it('Should print more than one binary subtraction expression', async () => {
        const expect = await _expect(`
            (println (- 2 3) (- 5 3))
        `);
        expect.stdout.to.equal('-1\n2\n');
    });
    it('Should print a binary multiplication expression', async () => {
        const expect = await _expect(`
            (println (* 100 (- 3)))
        `);
        expect.stdout.to.equal('-300\n');
    });
    it('Should print more than one binary multiplication expression', async () => {
        const expect = await _expect(`
            (println (* 5 (- 9)) (* 8 3))
        `);
        expect.stdout.to.equal('-45\n24\n');
    });
    it('Should print a binary division expression', async () => {
        const expect = await _expect(`
            (println (/ 5 2))
        `);
        expect.stdout.to.equal('2\n');
    });
    it('Should print more than one binary division expression', async () => {
        const expect = await _expect(`
            (println (/ 2 20) (/ (- 16) 4))
        `);
        expect.stdout.to.equal('0\n-4\n');
    });
    // TODO: Should be able to print ==
    // TODO: Should be able to print more than one ==
    // TODO: Should be able to print <
    // TODO: Should be able to print more than one <
    // TODO: Should be able to print <=
    // TODO: Should be able to print more than one <=
    // TODO: Should be able to print >
    // TODO: Should be able to print more than one >
    // TODO: Should be able to print >=
    // TODO: Should be able to print more than one >=
    it('Should not be able to print a print expression', async () => {
        const expect = await _expect(`
            (println (println 42))
        `);
        expect.stderr.to.contain('Cannot print operand \'println\' because it does not evaluate to a known type');
        expect.stderr.to.contain('1 Error');
    });
    it('Should not be able to print more than one print expression', async () => {
        const expect = await _expect(`
            (println (println 42) (println (- 42)))
        `);
        expect.stderr.to.contain('Cannot print operand \'println\' because it does not evaluate to a known type');
        expect.stderr.to.contain('2 Errors');
    });
    it('Should not be able to print an empty let expression', async () => {
        const expect = await _expect(`
            (println (let []))
        `);
        expect.stderr.to.contain('Cannot print operand \'let\' because it does not evaluate to a known type');
        expect.stderr.to.contain('1 Error');
    });
    it('Should not be able to print more than one empty let expression', async () => {
        const expect = await _expect(`
            (println (let []) (let []))
        `);
        expect.stderr.to.contain('Cannot print operand \'let\' because it does not evaluate to a known type');
        expect.stderr.to.contain('2 Errors');
    });
    it('Should not be able to print let expressions that evaluate to nil', async () => {
        const expect = await _expect(`
            (println (let [] (let [])) (let [] (println 42)))
        `);
        expect.stderr.to.contain('Cannot print operand \'let\' because it does not evaluate to a known type');
        expect.stderr.to.contain('2 Errors');
    });
    it('Should print a let expression that evaluates to an integer', async () => {
        const expect = await _expect(`
            (println (let [] 42))
        `);
        expect.stdout.to.equal('42\n');
    });
    it('Should print more than one let expression that evaluates to an integer', async () => {
        const expect = await _expect(`
            (println (let [] 69) (let [] 420))
        `);
        expect.stdout.to.equal('69\n420\n');
    });
    // TODO: Should print a let expression that evaluates to a boolean literal true
    // TODO: Should print a let expression that evaluates to more than one boolean literal true
    // TODO: Should print a let expression that evaluates to a boolean literal false
    // TODO: Should print a let expression that evaluates to more than one boolean literal false
    it('Should print a let expression that evaluates to an identifier', async () => {
        const expect = await _expect(`
            (println (let [a 5] a))
        `);
        expect.stdout.to.equal('5\n');
    });
    it('Should print a let expression that evaluates to a unary expression', async () => {
        const expect = await _expect(`
            (println (let [] (- 3)))
        `);
        expect.stdout.to.equal('-3\n');
    });
    it('Should print a let expression that evaluates to a binary addition expression', async () => {
        const expect = await _expect(`
            (println (let [] (+ 2 2)))
        `);
        expect.stdout.to.equal('4\n');
    });
    it('Should print a let expression that evaluates to a binary subtraction expression', async () => {
        const expect = await _expect(`
            (println (let [] (- 3 5)))
        `);
        expect.stdout.to.equal('-2\n');
    });
    it('Should print a let expression that evaluates to a binary multiplication expression', async () => {
        const expect = await _expect(`
            (println (let [] (* 1 6)))
        `);
        expect.stdout.to.equal('6\n');
    });
    it('Should print a let expression that evaluates to a binary division expression', async () => {
        const expect = await _expect(`
            (println (let [] (/ 7 3)))
        `);
        expect.stdout.to.equal('2\n');
    });
    // TODO: Should print a let expression that evaluates ==
    // TODO: Should print a let expression that evaluates <
    // TODO: Should print a let expression that evaluates <=
    // TODO: Should print a let expression that evaluates >
    // TODO: Should print a let expression that evaluates >=
});