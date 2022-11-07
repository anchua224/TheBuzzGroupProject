import { render, screen } from '@testing-library/react';
import comments from '../Comments';

test('render the comments',()=>{
    render(<Comments/>);
    const com = screen.getByTestId('com');
    expect(com).toBeInTheDocument();
});