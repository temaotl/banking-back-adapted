package eu.greyson.sample.data.dto.transaction.converters;

import eu.greyson.sample.data.dto.transaction.TransactionDTO;
import eu.greyson.sample.data.entity.Transaction;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class DtoToTransactionConverter implements Function<TransactionDTO, Transaction> {

    private final ModelMapper modelMapper;

    @Autowired
    public DtoToTransactionConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Transaction apply(TransactionDTO transactionDTO) {
        return modelMapper.map(transactionDTO, Transaction.class);
    }
}