package net.andreinc.mockneat.unit.networking;

/**
 * Copyright 2017, Andrei N. Ciobanu

 Permission is hereby granted, free of charge, to any user obtaining a copy of this software and associated
 documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 persons to whom the Software is furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 OTHERWISE, ARISING FROM, FREE_TEXT OF OR PARAM CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS PARAM THE SOFTWARE.
 */

import net.andreinc.mockneat.MockNeat;
import net.andreinc.mockneat.abstraction.MockUnitBase;
import net.andreinc.mockneat.abstraction.MockUnitString;
import net.andreinc.mockneat.types.Range;
import net.andreinc.mockneat.types.enums.IPv4Type;

import java.util.Arrays;
import java.util.function.Supplier;

import static net.andreinc.mockneat.types.enums.IPv4Type.NO_CONSTRAINT;
import static net.andreinc.mockneat.utils.ValidationUtils.notEmptyOrNullValues;
import static net.andreinc.mockneat.utils.ValidationUtils.notNull;

public class IPv4s extends MockUnitBase implements MockUnitString {

    public IPv4s(MockNeat mockNeat) {
        super(mockNeat);
    }

    @Override
    public Supplier<String> supplier() {
        return type(NO_CONSTRAINT).supplier();
    }

    public MockUnitString types(IPv4Type... types) {
        notEmptyOrNullValues(types, "types");
        IPv4Type type = mockNeat.from(types).val();
        return type(type);
    }

    public MockUnitString type(IPv4Type type) {
        notNull(type, "type");
        Range<Integer>[] oc = type.getOctets();
        Supplier<String> supp = () -> {
            StringBuilder buff = new StringBuilder();
            Arrays.stream(oc).forEach(range -> {
                int low = range.getLowerBound();
                int up = range.getUpperBound();
                if (range.isConstant()) buff.append(low).append(".");
                else {
                    int result = mockNeat.ints().range(low, up + 1).val();
                    buff.append(result).append(".");
                }
            });
            buff.deleteCharAt(buff.length() - 1);
            return buff.toString();
        };
        return () -> supp;
    }

}
