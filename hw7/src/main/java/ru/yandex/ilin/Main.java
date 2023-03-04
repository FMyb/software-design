package ru.yandex.ilin;

import ru.yandex.ilin.expression.MyExpression;
import ru.yandex.ilin.expression.exceptions.DBZEEException;
import ru.yandex.ilin.expression.exceptions.IllegalArgumentException;
import ru.yandex.ilin.expression.exceptions.IllegalNumberException;
import ru.yandex.ilin.expression.exceptions.IllegalOperationException;
import ru.yandex.ilin.expression.exceptions.IllegalSymbolException;
import ru.yandex.ilin.expression.exceptions.OverflowEEException;
import ru.yandex.ilin.expression.generic.Calculator;
import ru.yandex.ilin.expression.generic.DoubleCalculator;
import ru.yandex.ilin.expression.parser.ExpressionParser;
import ru.yandex.ilin.profiler.Profiler;

public class Main {
    public static void main(String[] args) throws
                                           IllegalSymbolException,
                                           OverflowEEException,
                                           DBZEEException,
                                           IllegalOperationException,
                                           IllegalNumberException,
                                           IllegalArgumentException {
        Profiler profiler = Profiler.getInstance();
        profiler.setPackagePrefix("ru.yandex.ilin.expression");
        try {
            String s = "10000000 * x * y * 10000000 + z";
            System.out.println("Выражение для разбора: " + s);
//        (x << (1593491994 - ((-1 * x) - ((x / 1866500826) * -2034520121))))*(x << (1593491994 - ((-1 * x) - ((x / 1866500826) * -2034520121))))
            Calculator<Double> calc = new DoubleCalculator();
            ExpressionParser<Double> ep = calc.getParserType();
            MyExpression<Double> result = ep.parse(s, calc);
            System.out.println("Результат разбора: " + result.toString());
            System.out.println("Результат разбора в короткой форме: " + result.toMiniString());
            System.out.println(
                "Результат вычислений в Double при x = -5.0, y = -18.0, z = -19.0: " + result.calc(-5.0, -18.0, -19.0));
        } finally {
            profiler.printStats(System.out);
        }
    }
}
